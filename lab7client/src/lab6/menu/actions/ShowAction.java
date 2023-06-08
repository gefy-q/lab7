/*
Выводит элементы коллекции, сначала часть, потом можно выбрать, что именно нужно узнать
 */
package lab6.menu.actions;

import lab6.controllers.CollectionController;
import lab6.model.Dragon;
import lab6.representations.console.ConsoleDragonRepr;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

public class ShowAction extends Action {
    CollectionController controller;

    public ShowAction(CollectionController controller, String args, String description) {
        super(args, description);
        this.controller = controller;
    }

    @Override
    public boolean process(Scanner scanner, Writer writer) throws IOException {
        if (controller.isEmpty()) {
            writer.write("No dragons found\n");
            writer.flush();
            return true;
        }
        ArrayList<Dragon> dragons = new ArrayList<>();
        for (Dragon dragon : controller) {
            dragons.add(dragon);
        }
        ConsoleDragonRepr.show(scanner, writer, dragons);
        return true;
    }
}
