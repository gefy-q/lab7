package lab6.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L+3L;
    private int id;
    private String username;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + '}';
    }
}
