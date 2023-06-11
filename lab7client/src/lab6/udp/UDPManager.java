package lab6.udp;

import java.io.IOException;
import lab6.Main;
import lab6.model.User;
import lab6.model.UserCredentials;

public class UDPManager {
    private Client client;


    public UDPManager() throws IOException {
        client = new Client();
    }
    public ServerCommand send(ServerCommand command) {
        ServerCommand response = sendRaw(command);
        if(response.type == ServerCommandType.ERROR && response.data != null)
            throw new UDPException((String)Utils.deserializeObject(response.data));
        return response;
    }
    public ServerCommand sendRaw(ServerCommand command) {
        if(command.userCredentials == null)
            command.userCredentials = Main.getCredentials();
        try {
            byte[] response = client.sendMsg(Utils.serializeObject(command));
            if(response == null)
                return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("Произошла ошибка во время получения пакетов с сервера"));
            return (ServerCommand) Utils.deserializeObject(response);
        } catch (IOException e) {
            return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("Превышено время ожидания ответа от сервера"));
        }
    }
    public User auth(UserCredentials credentials) {
        ServerCommand response = send(new ServerCommand(ServerCommandType.AUTH, null, credentials));
        if(response.type == ServerCommandType.ERROR) {
            if(response.data != null)
                throw new UDPException((String)Utils.deserializeObject(response.data));
            throw new UDPException("Неверный логин или пароль");
        }
        return (User)Utils.deserializeObject(response.data);
    }
    public void register(UserCredentials credentials) {
        if(credentials.getUsername().length() < 3 || credentials.getPassword().length() < 3)
            throw new RuntimeException("Логин или пароль слишком короткие");
        ServerCommand response = send(new ServerCommand(ServerCommandType.REGISTER, null, credentials));
        if(response.type == ServerCommandType.ERROR) {
            if(response.data != null)
                throw new UDPException((String)Utils.deserializeObject(response.data));
            throw new UDPException("Пользователь с данным логином уже существует");
        }
    }
}
