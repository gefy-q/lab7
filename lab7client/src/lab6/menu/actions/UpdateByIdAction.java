/*
Изменение id дракончика
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

public class UpdateByIdAction extends Action {
    private CollectionController controller;

    public UpdateByIdAction(CollectionController controller, String args, String description) {
        super(args, description);
        this.controller = controller;
    }

    @Override
    public boolean process(Scanner scanner, Writer writer) throws IOException {
        if (!scanner.hasNextInt()) {
            scanner.next();
            writer.write("Id must be positive integer\n");
            writer.flush();
            return true;
        }

        int id = scanner.nextInt();
        if (id < 0) {
            writer.write("Id must be positive integer\n");
            writer.flush();
            return true;
        }

        if (!controller.containsId(id)) {
            writer.write(String.format("Dragon with id \"%d\" not found\n", id));
            writer.flush();
            return true;
        }

        writer.write("Update dragon\n");
        writer.flush();

        controller.updateById(id, ConsoleDragonRepr.readAsDragon(scanner, writer));
        return true;
    }
}
