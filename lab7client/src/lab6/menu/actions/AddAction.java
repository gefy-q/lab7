/*
Действие для добавления элемента в коллекцию
 */
package lab6.menu.actions;

import lab6.controllers.CollectionController;
import lab6.model.Coordinates;
import lab6.model.Dragon;
import lab6.model.DragonCave;
import lab6.representations.console.ConsoleCoordinatesRepr;
import lab6.representations.console.ConsoleDragonCaveRepr;
import lab6.representations.console.ConsoleDragonRepr;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class AddAction extends Action {
    private final CollectionController controller;

    public AddAction(CollectionController controller, String args, String description) {
        super(args, description);
        this.controller = controller;
    }

    @Override
    public boolean process(Scanner scanner, Writer writer) throws IOException {
        writer.write("Enter dragon fields\n");
        writer.flush();
        controller.add(ConsoleDragonRepr.readAsDragon(scanner, writer));
        writer.write("Dragon has been added\n");
        writer.flush();
        return true;
    }
}
