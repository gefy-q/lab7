package lab6.server;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import lab6.controllers.CollectionController;
import lab6.model.Dragon;
import lab6.representations.json.JsonCollectionControllerRepr;
import lab6.udp.ChunksData;
import lab6.udp.ServerCommand;
import lab6.udp.ServerCommandType;
import lab6.udp.Utils;

public class Server {
    public static final int PACKET_SIZE = 5*1024-8;
    private Map<String, ChunksData> clientChunks = new ConcurrentHashMap<>();
    
    private DatagramSocket socket;
    private ForkJoinPool requestReadPool;
    private static final int READ_POOL_SIZE = 20;
    private ExecutorService analyzeThreadPool;
    private ExecutorService responseThreadPool;
    private List<Future<?>> futures = new CopyOnWriteArrayList<>();
    
    private CollectionController controller = null;
    private Path path = null;
    
    public Server(Path path, CollectionController controller) {
        this.path = path;
        this.controller = controller;
    }
    public void run() throws IOException {
        requestReadPool = new ForkJoinPool(READ_POOL_SIZE);
        analyzeThreadPool = Executors.newCachedThreadPool();
        responseThreadPool = Executors.newCachedThreadPool();

        Scanner scanner = new Scanner(System.in);
        int port;
        while (true){
            System.out.println("Введите порт: ");
            try{
                port = Integer.parseInt(scanner.nextLine());
                if(available(port)){
                    socket = new DatagramSocket(port);
                    break;
                }
            }catch (Exception e){
                System.out.println("Порт не подходит");
            }
        }
        System.out.println("Сервер проснулся");


        Runnable check = () -> {
            while (true) {
                for (String key : clientChunks.keySet()) {
                    if (clientChunks.get(key).isReady()) {
                        // YAY WE GOT IT
                        byte[] requestFull = clientChunks.get(key).getFullResponse();
                        System.out.println("(Thread Analyzing) Получен полный запрос клиента длиной в " + requestFull.length + " байт.");
                        ServerCommand toExecute = (ServerCommand)Utils.deserializeObject(requestFull);
                        /*User caller = Main.getStorageManager().authorizeUser(toExecute.userCredentials);
                        ServerCommand tempMessage = new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("Нельзя выполнять команды неавторизованным пользователем"));
                        if(caller != null || toExecute.type == ServerCommandType.AUTH || toExecute.type == ServerCommandType.REGISTER)
                            tempMessage = executeInput(toExecute, caller);*/
                        final ServerCommand message = executeInput(toExecute/*, caller*/);
                        // Отправка ответа на клиентскую сторону
                        List<byte[]> chunks = splitByteArray(Utils.serializeObject(message));
                        for (int i = 0; i < chunks.size(); i++) {
                            int index = i;
                            // Отправка ответа на клиентскую сторону
                            futures.add(responseThreadPool.submit(() -> {
                                List<byte[]> chunkInfo = new ArrayList<>();
                                chunkInfo.add(Utils.intToBytes(chunks.size()));
                                chunkInfo.add(Utils.intToBytes(index));
                                chunkInfo.add(chunks.get(index));
                                ByteBuffer responseBuffer = ByteBuffer.wrap(Utils.joinByteArrays(chunkInfo));
                                DatagramPacket responseInf = new DatagramPacket(
                                        responseBuffer.array(),
                                        responseBuffer.array().length,
                                        clientChunks.get(key).getPacket().getSocketAddress()
                                );
                                // отправка ответа
                                try {
                                    socket.send(responseInf);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                System.out.println("(Thread Sending) Отправлен чанк длиной " + responseBuffer.array().length + " байт клиенту " + clientChunks.get(key).getClientAddress().toString());
                            }));
                        }
                        boolean allTasksCompleted = false;
                        while (!allTasksCompleted) {
                            allTasksCompleted = true;
                            for (Future<?> future : futures) {
                                if (!future.isDone()) {
                                    allTasksCompleted = false;
                                    break;
                                }
                            }
                        }
                        futures.clear();
                        clientChunks.remove(key);
                        System.out.println("(Thread Analyzing) "+key+" удален.");
                    }
                }
            }
        };
        analyzeThreadPool.execute(check);
        
        // Ожидаем сообщения от клиента
        while (true) {
            byte[] bufferHelp = new byte[PACKET_SIZE+8];
            DatagramPacket request = new DatagramPacket(bufferHelp, bufferHelp.length);
            socket.receive(request);

            // читаем запросы
            requestReadPool.execute(()->{
                System.out.println("(Thread Reading) поток приступил к работе");
                SocketAddress clientAddress = request.getSocketAddress();
                byte[] recieved = request.getData();
                System.out.println("(Thread Reading) Получен чанк длины "+recieved.length+" байт от клиента "+clientAddress.toString());

                int index = Utils.fromByteArray(Arrays.copyOfRange(recieved, 4, 8));
                byte[] data = Arrays.copyOfRange(recieved, 8, recieved.length);
                
                if(clientChunks.containsKey(clientAddress.toString())) {
                    try {
                        clientChunks.get(clientAddress.toString()).addChunk(index, data);
                        System.out.println("(Thread Reading) Чанк добавлен к имеющимся и готовность: "+clientChunks.get(clientAddress.toString()).isReady());
                    } catch(Exception ex)  {
                        int size = Utils.fromByteArray(Arrays.copyOfRange(recieved, 0, 4));
                        ChunksData chunksData = new ChunksData(size);
                        clientChunks.put(clientAddress.toString(), chunksData);
                        clientChunks.get(clientAddress.toString()).setClientAddress(clientAddress);
                        clientChunks.get(clientAddress.toString()).setPacket(request);
                        clientChunks.get(clientAddress.toString()).addChunk(index, data);
                        System.out.println("(Thread Reading) Чанк добавлен как новый и готовность: "+clientChunks.get(clientAddress.toString()).isReady()+" так как "+clientChunks.get(clientAddress.toString()).getSize()+", а всего "+clientChunks.get(clientAddress.toString()).getChunks().size());
                    }
                }else{
                    int size = Utils.fromByteArray(Arrays.copyOfRange(recieved, 0, 4));
                    ChunksData chunksData = new ChunksData(size);
                    clientChunks.put(clientAddress.toString(), chunksData);
                    clientChunks.get(clientAddress.toString()).setClientAddress(clientAddress);
                    clientChunks.get(clientAddress.toString()).setPacket(request);
                    clientChunks.get(clientAddress.toString()).addChunk(index, data);
                    System.out.println("(Thread Reading) Чанк добавлен как новый и готовность: "+clientChunks.get(clientAddress.toString()).isReady()+" так как "+clientChunks.get(clientAddress.toString()).getSize()+", а всего "+clientChunks.get(clientAddress.toString()).getChunks().size());
                }
            });
            while(requestReadPool.getActiveThreadCount()!=0){}
        }
    }
    /**
     * Execute action
     * @param action Command with arguments
     * @return Result
     */
    public final ServerCommand executeInput(ServerCommand action) {
        ServerCommandType type = action.type;
        byte[] args = action.data;
        try {
            switch (type) {
                case ADD:
                    controller.add((Dragon)Utils.deserializeObject(args));
                    save();
                    return new ServerCommand(ServerCommandType.ADD, null);
                case ADD_IF_MAX:
                    controller.addIfMax((Dragon)Utils.deserializeObject(args));
                    save();
                    return new ServerCommand(ServerCommandType.ADD_IF_MAX, null);
                case CLEAR:
                    controller.clear();
                    save();
                    return new ServerCommand(ServerCommandType.CLEAR, null);
                case COUNT_LESS_THAN_WINGSPAN:
                    return new ServerCommand(ServerCommandType.COUNT_LESS_THAN_WINGSPAN, Utils.intToBytes(controller.countLessThanWingspan((Double)Utils.deserializeObject(args))));
                case COUNT_BY_AGE:
                    return new ServerCommand(ServerCommandType.COUNT_BY_AGE, Utils.intToBytes(controller.countByAge(Utils.fromByteArray(args))));
                case FIND_INDEX:
                    return new ServerCommand(ServerCommandType.FIND_INDEX, Utils.intToBytes(controller.findIndexById(Utils.fromByteArray(args))));
                case GET_INDEX:
                    return new ServerCommand(ServerCommandType.GET_INDEX, Utils.intToBytes(controller.getIndexById(Utils.fromByteArray(args))));
                case GET_INIT:
                    return new ServerCommand(ServerCommandType.GET_INIT, Utils.serializeObject(controller.getInitTime()));
                case INSERT:
                    ArrayList<Object> insertArgs = (ArrayList<Object>)Utils.deserializeObject(args);
                    controller.insertAt((int)insertArgs.get(0), (Dragon)insertArgs.get(1));
                    save();
                    return new ServerCommand(ServerCommandType.INSERT, null);
                case IS_EMPTY:
                    return new ServerCommand(ServerCommandType.IS_EMPTY, new byte[]{(byte)(controller.isEmpty() ? 1 : 0)});
                case REMOVE:
                    controller.removeById(Utils.fromByteArray(args));
                    save();
                    return new ServerCommand(ServerCommandType.REMOVE, null);
                case REMOVE_GREATER:
                    controller.removeGreater((Dragon)Utils.deserializeObject(args));
                    save();
                    return new ServerCommand(ServerCommandType.REMOVE_GREATER, null);
                case SIZE:
                    return new ServerCommand(ServerCommandType.SIZE, Utils.intToBytes(controller.size()));
                case UPDATE:
                    ArrayList<Object> updateArgs = (ArrayList<Object>)Utils.deserializeObject(args);
                    controller.updateById((int)updateArgs.get(0), (Dragon)updateArgs.get(1));
                    save();
                    return new ServerCommand(ServerCommandType.UPDATE, null);
                case UPDATE_DATA:
                    return new ServerCommand(ServerCommandType.UPDATE_DATA, Utils.serializeObject(controller.getCollection()));
                default:
                    return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("Неизвестная команда для сервера"));
            }
        } catch(Exception ex) {
            return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject(ex.getMessage()));
        }
    }

    public static boolean available(int port) {
        if (port < 1023 || port > 65534) {
            return false;
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    public static List<byte[]> splitByteArray(byte[] source) {
        int maxChunkSize = PACKET_SIZE;
        if (source.length <= maxChunkSize) {
            return Collections.singletonList(source);
        }
        int numChunks = (int) Math.ceil((double) source.length / maxChunkSize);
        List<byte[]> chunks = new ArrayList<>(numChunks);

        int offset = 0;
        for (int i = 0; i < numChunks; i++) {
            int chunkSize = Math.min(maxChunkSize, source.length - offset);
            byte[] chunk = Arrays.copyOfRange(source, offset, offset + chunkSize);
            chunks.add(chunk);
            offset += chunkSize;
        }

        return chunks;
    }
    
    public boolean save() throws IOException {
        try (BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path.toString())))) {
            JsonCollectionControllerRepr.write(fileWriter, controller);
            System.out.println("Successfully saved");
            fileWriter.flush();
        } catch (IOException e) {}
        return true;
    }
}
