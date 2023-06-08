package lab6.udp;

import java.io.Serializable;

public class ServerCommand implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L-10;
    public ServerCommandType type;
    public byte[] data;
    public ServerCommand(ServerCommandType type, byte[] data) {
        this.type = type;
        this.data = data;
    }
}
