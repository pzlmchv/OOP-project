package bg.tu_varna.ks.command.factories;

import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.command.Command;
import bg.tu_varna.ks.command.utility.Exit;
import bg.tu_varna.ks.command.utility.Help;

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
            case OPEN -> null;
            case CLOSE -> null;
            case SAVE -> null;
            case SAVE_AS -> null;
            case HELP -> new Help();
            case EXIT -> new Exit();
            case BOOK -> null;
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
