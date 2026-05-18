package bg.tu_varna.ks.command.utility;

import bg.tu_varna.ks.contracts.Executable;

/**
 * Command that terminates the application.
 * <p>
 * Usage: <code>exit</code>. Prints a message and immediately calls
 * {@link System#exit(int)} with status code {@code 0}. This is the only
 * normal way to leave the REPL loop in
 * {@link bg.tu_varna.ks.Application}.
 * </p>
 *
 * <p>
 * <b>Note:</b> the command does not automatically save the currently
 * opened file. The user must execute {@code save} before {@code exit}
 * if they want their changes preserved.
 * </p>
 */
public class Exit implements Executable {

    /**
     * Executes the {@code exit} operation - prints a message and
     * terminates the JVM with status code {@code 0}.
     */
    @Override
    public void execute() {
        System.out.println("Exiting app!");
        System.exit(0);
    }
}
