package bg.tu_varna.ks.command.factories;

import bg.tu_varna.ks.command.events.Book;
import bg.tu_varna.ks.command.utility.*;
import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.command.Command;

import java.util.List;

public class CommandFactory {
    private static CommandFactory instance;

    private CommandFactory() {
    }

    public static CommandFactory getInstance() {
        if (instance == null) {
            instance = new CommandFactory();
        }

        return instance;
    }

    public Executable getExecutable(Command command, List<String> arguments) {
        return switch (command) {
            case OPEN -> new Open(arguments);
            case CLOSE -> new Close();
            case SAVE -> new Save();
            case SAVE_AS -> new SaveAs(arguments);
            case HELP -> new Help();
            case EXIT -> new Exit();
            case BOOK -> new Book(arguments);
            case UNBOOK -> null;
            case AGENDA -> null;
            case CHANGE -> null;
            case FIND -> null;
            case HOLIDAY -> null;
            case BUSYDAYS -> null;
            case FINDSLOT -> null;
            case FINDSLOTWITH -> null;
            case MERGE -> null;
        };
    }
}
