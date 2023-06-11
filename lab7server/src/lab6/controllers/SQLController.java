package lab6.controllers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLController {
    private final String url = "jdbc:postgresql://localhost/studs";
    private String user = "postgres";
    private String password = "12345";
    public SQLController(Path credentials) {
        try {
            BufferedReader data = new BufferedReader(new InputStreamReader(new FileInputStream(credentials.toString())));
            user = data.readLine();
            password = data.readLine();
        } catch (FileNotFoundException ex) {
            System.out.println("Не удалось авторизоваться в PostgreSQL с предоставленными данными");
        } catch (IOException ex) {
            System.out.println("Не удалось авторизоваться в PostgreSQL с предоставленными данными");
        }
    }
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    public boolean send(PreparedStatement sql) {
        System.out.println(sql);
        try {
            sql.executeUpdate();
            sql.getConnection().close();
        } catch(SQLException ex) {
            return false;
        }
        return true;
    }
    public boolean sendRaw(String sql) {
        System.out.println(sql);
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.close();
        } catch(SQLException ex) {
            return false;
        }
        return true;
    }
    public ResultSet get(PreparedStatement sql) {
        System.out.println(sql);
        ResultSet result = null;
        try {
            result = sql.executeQuery();
            sql.getConnection().close();
        } catch(SQLException ex) {}
        return result;
    }
    public ResultSet getRaw(String sql) {
        System.out.println(sql);
        ResultSet result = null;
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            result = statement.executeQuery(sql);
            connection.close();
        } catch(SQLException ex) {}
        return result;
    }
}
