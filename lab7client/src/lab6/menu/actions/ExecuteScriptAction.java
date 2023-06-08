/*
Для работы со скриптом и выполнения команд оттуда. Перенаправляет действия в menu и работает, как с вводом данных
 */

package lab6.menu.actions;

import java.io.*;
import java.util.Scanner;

import lab6.menu.Menu;

public class ExecuteScriptAction extends Action {
    Menu menu;

    public ExecuteScriptAction(Menu menu, String args, String description) {
        super(args, description);
        this.menu = menu;
    }

    @Override
    public boolean process(Scanner scanner, Writer writer) throws IOException {
        menu.run(new InputStreamReader(new FileInputStream(scanner.next())), writer);
        return true;
    }
}
