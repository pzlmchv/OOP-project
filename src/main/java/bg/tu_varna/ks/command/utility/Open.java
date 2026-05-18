package bg.tu_varna.ks.command.utility;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;

import bg.tu_varna.ks.models.Calendar;
import jakarta.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

/**
 * Command that opens a calendar file and loads its contents.
 * <p>
 * Usage: <code>open &lt;file-name&gt;</code>. If the given name does
 * not contain a directory separator, the file is looked up in the
 * project's default directory
 * (<code>src/main/java/bg/tu_varna/ks/files/</code>). If the file does
 * not exist, it is created automatically with an empty calendar.
 * </p>
 * <p>
 * The command does not allow more than one file to be open at the
 * same time - attempting to open a second file produces an error.
 * </p>
 *
 * @see AppData#load(File)
 */
public class Open implements Executable {

    /** Arguments passed to the command from user input. */
    private final List<String> arguments;

    /** Default directory used when looking up files by bare name. */
    private final String PATH = ".\\src\\main\\java\\bg\\tu_varna\\ks\\files\\";

    /**
     * Constructs a new {@code open} command with the given arguments.
     *
     * @param arguments argument list (exactly one is expected - the
     *                  path or file name)
     */
    public Open(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Executes the {@code open} operation.
     * <p>
     * Validates the argument count and current application state, then
     * delegates the actual file loading to {@link AppData#load(File)}.
     * Errors are printed to {@link System#err}.
     * </p>
     */
    @Override
    public void execute() {
        if (!Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("file already open");
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
            AppData.getInstance().load(file);
            System.out.println("File opened successfully");
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            System.err.println("no file found");
        }
    }
}
