/*
Удаляет элемент с определенной позиции
 */
package lab6.menu.actions;

import lab6.controllers.CollectionController;

import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public class RemoveByIdAction extends Action {
    private final CollectionController controller;

    public RemoveByIdAction(CollectionController controller, String args, String description) {
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
        controller.removeById(id);

        writer.write("Dragon was successfully removed\n");
        writer.flush();
        return true;
    }
}
