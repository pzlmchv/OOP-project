package bg.tu_varna.ks.command.utility;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;

import jakarta.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

/**
 * Command that writes the current calendar state to a new file
 * specified by the user.
 * <p>
 * Usage: <code>saveas &lt;file-name&gt;</code>. If the given name does
 * not contain a directory separator, the file is written under the
 * project's default directory
 * (<code>src/main/java/bg/tu_varna/ks/files/</code>).
 * </p>
 * <p>
 * In order for a file to be saved, another file must currently be open
 * (the command operates against the "current document").
 * </p>
 *
 * @see AppData#save(File)
 */
public class SaveAs implements Executable {

    /** Arguments passed to the command from user input. */
    private final List<String> arguments;

    /** Default directory used when saving files by bare name. */
    private final String PATH = ".\\src\\main\\java\\bg\\tu_varna\\ks\\files\\";

    /**
     * Constructs a new {@code saveas} command with the given arguments.
     *
     * @param arguments argument list (exactly one is expected - the
     *                  path or file name)
     */
    public SaveAs(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Executes the {@code saveas} operation.
     * <p>
     * Validates the argument count and the presence of an open file,
     * then serializes the current calendar to the new specified file.
     * </p>
     */
    @Override
    public void execute() {
        if (Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("file not opened");
            return;
        }

        if (arguments.size() > 1) {
            System.err.println("too many arguments");
            return;
        }

        if (arguments.isEmpty()) {
            System.err.println("no arguments found");
            return;
        }

        File file = (arguments.get(0).contains(File.separator))
                ? new File(arguments.get(0))
                : new File(PATH.concat(arguments.get(0)));

        try {
            AppData.getInstance().save(file);
            System.out.println("File saved successfully");
        } catch (JAXBException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
