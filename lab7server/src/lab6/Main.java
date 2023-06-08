package lab6;

/*
Считывает файл по полученному пути и заполняет коллекцию, если файла нет - возвращает ошибку
После вызывает view-menu где происходят основные действия
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lab6.controllers.CollectionController;
import lab6.controllers.ArrayListController;
import lab6.model.Coordinates;
import lab6.model.Dragon;
import lab6.model.DragonCave;
import lab6.model.DragonCharacter;
import lab6.representations.json.JsonCollectionControllerRepr;
import lab6.representations.json.JsonDragonRepr;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import lab6.server.Server;


public class Main {
    public static void main(String[] args) {
        /*if (args.length != 1) {
            System.out.println("Usage: program <data-filename>");
            return;
        }*/

        final Path dataFile = Paths.get("D:/data.json"); // "C:\\Users\\ASUS\\OneDrive\\Рабочий стол\\учеба\\прога\\352.json"
        CollectionController controller = new ArrayListController();

        try {
            JsonCollectionControllerRepr.read(new InputStreamReader(new FileInputStream(dataFile.toString())), controller);
        } catch (IOException e) {
            System.err.println("Error: Cannot read data file.");
            readFile(controller);
        }
        Server server = new Server(dataFile, controller);
        try {
            server.run();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void readFile(CollectionController controller) {
        try {
            Scanner scanner = new Scanner(System.in);
            String name = readName(scanner);
            JsonCollectionControllerRepr.read(new InputStreamReader(new FileInputStream(name.toString())), controller);
        } catch (IOException e) {
            System.err.println("Error: Cannot read data file.");
            readFile(controller);
        }
    }

    private static String readName(Scanner scanner) throws IOException {
        while (true) {
            String name;
            System.err.println("Enter path to data file: ");
            while (scanner.hasNextLine()) {
                name = scanner.nextLine().trim();
                if (name.isEmpty()) {
                    System.err.println("It cannot be empty");
                    continue;
                }
                return name;
            }
        }
    }
}