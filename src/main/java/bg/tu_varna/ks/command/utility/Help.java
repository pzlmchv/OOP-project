package bg.tu_varna.ks.command.utility;

import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.command.Command;

/**
 * Command that prints the list of all supported commands and their
 * descriptions.
 * <p>
 * Usage: <code>help</code>. The information is taken directly from the
 * values of the {@link Command} enum.
 * </p>
 *
 * @see Command
 */
public class Help implements Executable {

    /**
     * Executes the {@code help} operation - iterates over all
     * {@link Command} values and prints each name and description in
     * an aligned, two-column layout.
     */
    @Override
    public void execute() {
        for (Command command : Command.values()) {
            System.out.printf("%-20s%s\n", command.getName().toUpperCase(), command.getDescription());
        }
    }
}
