/*
Очищает коллекцию
 */

package lab6.menu.actions;

import lab6.controllers.CollectionController;

import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public class ClearAction extends Action {
    private final CollectionController controller;

    public ClearAction(CollectionController controller, String args, String description) {
        super(args, description);
        this.controller = controller;
    }

    @Override
    public boolean process(Scanner scanner, Writer writer) {
        controller.clear();
        return true;
    }
}
