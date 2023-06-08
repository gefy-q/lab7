/*
Выводит все команды, которые получает из менюшки
 */

package lab6.menu.actions;

import lab6.menu.Menu;

import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public class HelpAction extends Action {
    private final Menu menu;

    public HelpAction(Menu menu, String args, String description) {
        super(args, description);
        this.menu = menu;
    }

    @Override
    public boolean process(Scanner scanner, Writer writer) throws IOException {
        menu.showInstructions(writer);
        return true;
    }
}
