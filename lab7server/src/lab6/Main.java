package lab6;

/**
* Считывает файл по полученному пути и заполняет коллекцию, если файла нет - возвращает ошибку
* После вызывает view-menu где происходят основные действия
 */

import lab6.controllers.ArrayListController;
import lab6.model.Coordinates;
import lab6.model.Dragon;
import lab6.model.DragonCave;
import lab6.model.DragonCharacter;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import lab6.controllers.SQLController;
import lab6.server.Server;
import lab6.controllers.ServerCollectionController;


public class Main {
    public static SQLController sqlController;
    public static void main(String[] args) {
        final Path credentialsFile = Paths.get("creds.txt"); // "C:\\Users\\ASUS\\OneDrive\\Рабочий стол\\учеба\\прога\\352.json"
        sqlController = new SQLController(credentialsFile);
        ServerCollectionController controller = new ArrayListController();
        Server server = new Server(controller);
        try {
            server.run();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}