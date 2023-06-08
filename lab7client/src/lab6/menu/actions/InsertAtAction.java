/*
Добавляет новый элемент, но на определенную позицию
 */
package lab6.menu.actions;

import lab6.controllers.CollectionController;
import lab6.representations.console.ConsoleDragonRepr;

import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public class InsertAtAction extends Action {
    private final CollectionController controller;

    public InsertAtAction(CollectionController controller, String args, String description) {
        super(args, description);
        this.controller = controller;
    }

    @Override
    public boolean process(Scanner scanner, Writer writer) throws IOException {
        if (!scanner.hasNextInt()) {
            writer.write("Index must be an positive integer\n");
            writer.flush();
            scanner.next();
            return true;
        }

        int index = scanner.nextInt();
        if (index < 0 || index >= controller.size()) {
            writer.write(String.format("Index must be in range [0, %d]", index));
            writer.flush();
            return true;
        }

        writer.write("Enter dragon fields\n");
        controller.insertAt(index, ConsoleDragonRepr.readAsDragon(scanner, writer));
        return true;
    }
}
