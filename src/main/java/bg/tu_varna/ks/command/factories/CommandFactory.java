package bg.tu_varna.ks.command.factories;

import bg.tu_varna.ks.command.events.*;
import bg.tu_varna.ks.command.utility.*;
import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.command.Command;

import java.util.List;

/**
 * Factory that creates concrete {@link Executable} objects according to
 * a given value of the {@link Command} enum.
 * <p>
 * It implements both the <b>Factory</b> and <b>Singleton</b> patterns.
 * This allows {@link bg.tu_varna.ks.command.CommandRunner} to work
 * declaratively: it provides a command name and a list of arguments and
 * receives back a ready-to-run implementation, without knowing its
 * concrete class.
 * </p>
 *
 * @see Command
 * @see Executable
 * @see bg.tu_varna.ks.command.CommandRunner
 */
public class CommandFactory {

    /** The single instance of this class (Singleton). */
    private static CommandFactory instance;

    /** Private constructor preventing external instantiation. */
    private CommandFactory() {
    }

    /**
     * Returns the single instance of the factory.
     *
     * @return the singleton instance of {@code CommandFactory}
     */
    public static CommandFactory getInstance() {
        if (instance == null) {
            instance = new CommandFactory();
        }

        return instance;
    }

    /**
     * Creates a new executable object that corresponds to the given
     * command.
     *
     * @param command the enum value describing the desired command
     * @param arguments the argument list for the command, extracted
     *                  from user input
     * @return a new {@link Executable} ready to be started via
     *         {@link Executable#execute()}
     */
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
