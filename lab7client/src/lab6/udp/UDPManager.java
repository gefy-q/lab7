package lab6.udp;

import java.io.IOException;

public class UDPManager {
    private Client client;


    public UDPManager() throws IOException {
        client = new Client();
    }
    public ServerCommand send(ServerCommand command) {
        ServerCommand response = sendRaw(command);
        if(response.type == ServerCommandType.ERROR)
            throw new UDPException((String)Utils.deserializeObject(response.data));
        return response;
    }
    public ServerCommand sendRaw(ServerCommand command) {
        try {
            byte[] response = client.sendMsg(Utils.serializeObject(command));
            if(response == null)
                return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("Произошла ошибка во время получения пакетов с сервера"));
            return (ServerCommand) Utils.deserializeObject(response);
        } catch (IOException e) {
            return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("Превышено время ожидания ответа от сервера"));
        }
    }
}
