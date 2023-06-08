/*
Класс - основа для действий.
*/
package lab6.menu.actions;

import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public abstract class Action {
    protected final String args;
    protected final String description;

    public Action(String args, String description) {
        this.args = args;
        this.description = description;
    }

    public String getArgs() {
        return args;
    }

    public String getDescription() {
        return description;
    }

    public abstract boolean process(Scanner scanner, Writer writer) throws IOException;
}
