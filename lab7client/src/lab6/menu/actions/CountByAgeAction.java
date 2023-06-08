/*
Получает число и считает, сколько дракончиков ему соответствует, отдает ответ
*/
package lab6.menu.actions;

import lab6.controllers.CollectionController;

import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public class CountByAgeAction extends Action {
    private final CollectionController controller;

    public CountByAgeAction(CollectionController controller, String args, String description) {
        super(args, description);
        this.controller = controller;
    }

    @Override
    public boolean process(Scanner scanner, Writer writer) throws IOException {
        while (true) {
            String name;
            writer.write("Enter name: ");
            writer.flush();
            while (scanner.hasNextLine()) {
                name = scanner.nextLine().trim();
                if (name.isEmpty()) {
                    writer.write("Age cannot be empty");
                    writer.write(System.lineSeparator());
                    writer.flush();
                    continue;
                }
                else if (!name.matches("\\d+")) {
                    writer.write("Age must be an positive integer\n");
                    writer.flush();
                    continue;
                }
                int age = Integer.parseInt(name);
                writer.write(String.format("With this age found %d dragons\n", controller.countByAge(age)));
                writer.flush();
                return true;
            }
        }
    }
}
