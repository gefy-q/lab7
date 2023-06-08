/*
Получение сокровищ и действия для вывода с ними
 */
package lab6.representations.console;

import lab6.model.DragonCave;

import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public class ConsoleDragonCaveRepr extends ConsoleRepr {
    private final long numberOfTreasures;

    public ConsoleDragonCaveRepr(long numberOfTreasures) {
        this.numberOfTreasures = numberOfTreasures;
    }

    public long getNumberOfTreasures() {
        return numberOfTreasures;
    }

    public static ConsoleDragonCaveRepr read(Scanner scanner, Writer writer) throws IOException {
        return new ConsoleDragonCaveRepr(readNumberOfTreasures(scanner, writer));
    }

    public static void show(Scanner scanner, Writer writer, DragonCave cave) throws  IOException {
        println(writer, "Number of treasures: " + cave.getNumberOfTreasures());
    }

    private static long readNumberOfTreasures(Scanner scanner, Writer writer) throws IOException {
        while (true) {
            print(writer, "Enter number of treasures: ");
            if (!scanner.hasNextLong()) {
                scanner.next();
                println(writer, "Number of treasures must be a positive integer");
                continue;
            }
            long numberOfTreasures = scanner.nextLong();
            if (numberOfTreasures <= 0) {
                println(writer, "Number of treasures must be a positive integer");
                continue;
            }
            return numberOfTreasures;
        }
    }
}
