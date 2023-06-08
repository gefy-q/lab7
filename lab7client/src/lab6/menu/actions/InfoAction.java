/*
Выводит информацию о коллекции
 */
package lab6.menu.actions;

import lab6.controllers.CollectionController;

import java.io.IOException;
import java.io.Writer;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class InfoAction extends Action {
    private final CollectionController controller;

    public InfoAction(CollectionController controller, String args, String description) {
        super(args, description);
        this.controller = controller;
    }

    @Override
    public boolean process(Scanner scanner, Writer writer) throws IOException {
        writer.write(String.format("Class: %s\n", controller.getClass().getName()));
        writer.write(String.format("Init time: %s\n", controller.getInitTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
        writer.write(String.format("Size: %s\n", controller.size()));
        writer.flush();
        return true;
    }
}
