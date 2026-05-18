package bg.tu_varna.ks.command.utility;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;

import java.util.Objects;

/**
 * Command that closes the currently opened calendar file without
 * saving any changes.
 * <p>
 * Usage: <code>close</code>. If no file is open, an error is printed.
 * </p>
 *
 * @see AppData#closeFile()
 */
public class Close implements Executable {

    /**
     * Executes the {@code close} operation.
     * <p>
     * Checks whether a file is currently active. If so, it is closed
     * and the calendar state is reset. Otherwise, an error message is
     * printed.
     * </p>
     */
    @Override
    public void execute() {
        if (Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("no open file to close");
            return;
        }

        AppData.getInstance().closeFile();
        System.out.println("File closed successfully!");
    }
}
