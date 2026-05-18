package bg.tu_varna.ks.contracts;

/**
 * Contract for any object that can be executed by the system.
 * <p>
 * This interface is the foundation of the <b>Command</b> pattern in the
 * project. Each concrete command (for example {@code Book}, {@code Save},
 * {@code Find}) implements {@link #execute()} and encapsulates its own
 * arguments and logic. This allows
 * {@link bg.tu_varna.ks.command.CommandRunner} and
 * {@link bg.tu_varna.ks.command.factories.CommandFactory} to work with
 * commands polymorphically, without knowing their concrete type.
 * </p>
 *
 * @see bg.tu_varna.ks.command.factories.CommandFactory
 * @see bg.tu_varna.ks.command.CommandRunner
 */
public interface Executable {

    /**
     * Executes the operation associated with the concrete command.
     * <p>
     * Implementations validate their own arguments, optionally inspect
     * the state of the currently opened file and/or the calendar, and
     * emit results or error messages on {@link System#out} or
     * {@link System#err}.
     * </p>
     */
    void execute();
}
