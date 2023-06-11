package lab6.model;

import java.io.Serializable;

public class UserCredentials implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L+4L;
    private String username;
    private String password;

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}
