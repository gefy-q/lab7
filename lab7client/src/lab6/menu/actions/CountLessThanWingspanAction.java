/*
Подсчитывает сколько дракончиков имеют меньший размах крыльев, чем введенный. Получает число, возвращает количество
 */

package lab6.menu.actions;

import lab6.controllers.CollectionController;

import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public class CountLessThanWingspanAction extends Action {
    private final CollectionController controller;

    public CountLessThanWingspanAction(CollectionController controller, String args, String description) {
        super(args, description);
        this.controller = controller;
    }

    @Override
    public boolean process(Scanner scanner, Writer writer) throws IOException {
        if (!scanner.hasNextInt()) {
            writer.write("Wingspan must be an positive double\n");
            writer.flush();
            scanner.next();
            return true;
        }

        double wingspan = scanner.nextDouble();
        writer.write(String.format("With wingspan less then this found %d dragons\n", controller.countLessThanWingspan(wingspan)));
        return true;
    }
}
