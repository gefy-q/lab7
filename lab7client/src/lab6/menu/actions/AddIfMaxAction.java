/*
Добавить элемент, если введенный, больше наибольшего в коллекции. Часть исполнения в ArrayListController.
 */

package lab6.menu.actions;

import lab6.controllers.CollectionController;
import lab6.model.Dragon;
import lab6.representations.console.ConsoleDragonRepr;

import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public class AddIfMaxAction extends Action {
    private final CollectionController controller;

    public AddIfMaxAction(CollectionController controller, String args, String description) {
        super(args, description);
        this.controller = controller;
    }

    @Override
    public boolean process(Scanner scanner, Writer writer) throws IOException {
        controller.addIfMax(ConsoleDragonRepr.readAsDragon(scanner, writer));
        return true;
    }
}
