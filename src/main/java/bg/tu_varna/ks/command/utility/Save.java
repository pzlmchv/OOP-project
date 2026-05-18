package bg.tu_varna.ks.command.utility;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;

import jakarta.xml.bind.JAXBException;
import java.util.Objects;

/**
 * Command that writes the current calendar state back to the active
 * file.
 * <p>
 * Usage: <code>save</code>. If no file is open, an error is printed.
 * </p>
 *
 * @see AppData#unload()
 */
public class Save implements Executable {

    /**
     * Executes the {@code save} operation.
     * <p>
     * Delegates the write to {@link AppData#unload()}. On JAXB errors,
     * the exception's message is printed to {@link System#err}.
     * </p>
     */
    @Override
    public void execute() {
        if (Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("no open file to save");
            return;
        }

        try {
            AppData.getInstance().unload();
            System.out.println("File saved successfully");
        } catch (JAXBException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
