package bg.tu_varna.ks.command.factories;

import bg.tu_varna.ks.command.events.*;
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
            case SAVEAS -> new SaveAs(arguments);
            case HELP -> new Help();
            case EXIT -> new Exit();
            case BOOK -> new Book(arguments);
            case UNBOOK -> new Unbook(arguments);
            case AGENDA -> new Agenda(arguments);
            case CHANGE -> new Change(arguments);
            case FIND -> new Find(arguments);
            case HOLIDAY -> new Holiday(arguments);
            case BUSYDAYS -> new BusyDays(arguments);
            case FINDSLOT -> new FindSlot(arguments);
            case FINDSLOTWITH -> new FindSlotWith(arguments);
            case MERGE -> new Merge(arguments);
        };
    }
}
