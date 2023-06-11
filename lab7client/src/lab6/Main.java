package lab6;

/*
Считывает файл по полученному пути и заполняет коллекцию, если файла нет - возвращает ошибку
После вызывает view-menu где происходят основные действия
 */

import lab6.controllers.CollectionController;
import lab6.menu.Menu;
import lab6.controllers.RemoteController;
import lab6.model.Coordinates;
import lab6.model.Dragon;
import lab6.model.DragonCave;
import lab6.model.DragonCharacter;
import lab6.ui.View;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import lab6.model.User;
import lab6.model.UserCredentials;
import lab6.udp.UDPManager;


public class Main {
    private static UDPManager udpManager = null;
    private static User currentUser = null;
    private static UserCredentials credentials = null;
    public static void main(String[] args) {

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(System.out));
        try {
            udpManager = new UDPManager();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        View view = new View(new RemoteController());
        view.run(inputReader, outputWriter);
    }

    public static UDPManager getUdpManager() {
        return udpManager;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Main.currentUser = currentUser;
    }

    public static UserCredentials getCredentials() {
        return credentials;
    }

    public static void setCredentials(UserCredentials credentials) {
        Main.credentials = credentials;
    }
    
}