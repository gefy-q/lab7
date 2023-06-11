/*
Информация по командам
 */
package lab6.ui;

import lab6.menu.actions.ExitAction;
import lab6.menu.actions.ExecuteScriptAction;
import lab6.menu.actions.RemoveGreaterAction;
import lab6.menu.actions.InfoAction;
import lab6.menu.actions.HelpAction;
import lab6.menu.actions.RemoveByIdAction;
import lab6.menu.actions.ShowAction;
import lab6.menu.actions.AddIfMaxAction;
import lab6.menu.actions.FilterStartsWithNameAction;
import lab6.menu.actions.CountByAgeAction;
import lab6.menu.actions.ClearAction;
import lab6.menu.actions.CountLessThanWingspanAction;
import lab6.menu.actions.UpdateByIdAction;
import lab6.menu.actions.AddAction;
import lab6.controllers.CollectionController;
import lab6.menu.Menu;

import java.io.*;
import java.nio.file.Path;
import java.util.Scanner;
import lab6.Main;
import lab6.model.User;
import lab6.model.UserCredentials;
import lab6.udp.UDPException;

public class View {
    private final Menu menu;
    CollectionController controller;

    public View(CollectionController controller) {
        this.controller = controller;
        menu = new Menu();
        menu.addAction("help", new HelpAction(menu, "", "show help information for available commands"));
        menu.addAction("info", new InfoAction(controller, "", "show collection info"));
        menu.addAction("show", new ShowAction(controller, "", "show dragons"));
        menu.addAction("add", new AddAction(controller, "{element}", "add new element"));
        menu.addAction("update", new UpdateByIdAction(controller, "id {element}", "update element by id"));
        menu.addAction("remove_by_id", new RemoveByIdAction(controller, "id", "remove element by id"));
        menu.addAction("clear", new ClearAction(controller, "", "clear collection"));
        menu.addAction("execute_script", new ExecuteScriptAction(menu, "file_name", "execute script from file"));
        menu.addAction("exit", new ExitAction("", "exit from program"));
        menu.addAction("add_if_max", new AddIfMaxAction(controller, "{element}", "insert element if is greater"));
        menu.addAction("remove_greater", new RemoveGreaterAction(controller, "{element}", "remove greater than this"));
        menu.addAction("count_by_age", new CountByAgeAction(controller, "age", "count dragons with this age"));
        menu.addAction("count_less_than_wingspan", new CountLessThanWingspanAction(controller, "wingspan", "count dragons with less wingspan"));
        menu.addAction("filter_starts_with_name", new FilterStartsWithNameAction(controller, "name", "filter dragons by name"));
    }

    public void run(Reader reader, Writer writer) {
        auth();
        controller.updateCollection();
        try {
            writer.write("Welcome! Enter the command. To show instructions type \"help\"\n");
            writer.flush();
            menu.run(reader, writer);
        } catch (IOException e) {
            System.err.println("Error. " + e.getMessage());
        }
    }
    public void auth() {
        if(Main.getCredentials() != null)
            return;
        while(true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Вы хотите авторизоваться или зарегестрироваться?");
                System.out.println("1 = Авторизоваться");
                System.out.println("2 = Зарегестрироваться");
                System.out.print("> ");
                String request = scanner.nextLine().strip();
                if("1".equals(request)) { // авторизация
                    System.out.print("Введите логин: ");
                    String login = scanner.nextLine().strip();
                    System.out.print("Введите пароль: ");
                    String password = scanner.nextLine();
                    UserCredentials credentials = new UserCredentials(login, password);
                    User auth = Main.getUdpManager().auth(credentials);
                    System.out.println("Успешная авторизация. Пользователь - "+auth.getUsername());
                    Main.setCredentials(credentials);
                    Main.setCurrentUser(auth);
                    break;
                } else if("2".equals(request)) { // регистрация
                    System.out.print("Введите логин: ");
                    String login = scanner.nextLine().strip();
                    System.out.print("Введите пароль: ");
                    String password = scanner.nextLine();
                    UserCredentials credentials = new UserCredentials(login, password);
                    Main.getUdpManager().register(credentials);
                    System.out.println("Пользователь "+login+" зарегестрирован! Для продолжения авторизуйтесь.");
                }
            } catch(UDPException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
