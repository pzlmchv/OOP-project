package bg.tu_varna.ks.command.files;

import bg.tu_varna.ks.contracts.FileHandler;
import bg.tu_varna.ks.models.Calendar;
import bg.tu_varna.ks.models.Event;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton responsible for reading and writing calendar data in
 * an XML file via JAXB.
 * <p>
 * The class holds a reference to the currently opened file and exposes
 * methods to load, save, and close it. It implements the
 * {@link FileHandler} contract by providing
 * {@link #load(File)} and {@link #unload()}.
 * </p>
 *
 * <p>
 * In addition to managing the "opened document", it also offers the
 * ability to read an <i>external</i> calendar file without making it
 * active - see {@link #readCalendar(File)}, used by the {@code merge}
 * and {@code findslotwith} commands.
 * </p>
 *
 * @see FileHandler
 * @see Calendar
 */
public class AppData implements FileHandler {

    /** The single instance of this class (Singleton). */
    private static AppData instance;

    /** Currently opened file; {@code null} if there is none. */
    private File file;

    /** JAXB context used for serialization/deserialization. */
    private JAXBContext context;

    /** Private constructor preventing external instantiation. */
    private AppData() {
    }

    /**
     * Returns the single instance of {@code AppData}.
     *
     * @return the singleton instance
     */
    public static AppData getInstance() {
        if (instance == null) {
            instance = new AppData();
        }
        return instance;
    }

    /**
     * Serializes the current state of {@link Calendar} to the given
     * file as formatted XML.
     *
     * @param file the target file
     * @throws JAXBException on serialization error
     */
    public void write(File file) throws JAXBException {
        context = JAXBContext.newInstance(Calendar.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(Calendar.getInstance(), file);
    }

    /**
     * Reads a calendar from an external file without making it active.
     * <p>
     * Used, for example, by the {@code merge} operation, which needs to
     * iterate over another calendar's events without replacing the
     * current state.
     * </p>
     *
     * @param file the file to read
     * @return the newly read {@link Calendar} object; never {@code null}
     * @throws JAXBException on deserialization error
     * @throws FileNotFoundException if the file does not exist
     */
    public Calendar readCalendar(File file) throws JAXBException, FileNotFoundException {
        context = JAXBContext.newInstance(Calendar.class);

        Calendar calendar = (Calendar) context.createUnmarshaller()
                .unmarshal(new FileReader(file.getAbsolutePath()));

        if (calendar.getEvents() == null) {
            calendar.setEvents(new ArrayList<>());
        }

        return calendar;
    }

    /**
     * Reads the contents of the file and applies them as the current
     * state of the {@link Calendar} singleton.
     *
     * @param file the file to read
     * @throws JAXBException on deserialization error
     * @throws FileNotFoundException if the file does not exist
     */
    public void read(File file) throws JAXBException, FileNotFoundException {
        Calendar.getInstance().setEvents(readCalendar(file).getEvents());
    }

    /**
     * Saves the current state of the calendar to the given file.
     * <p>Semantically an alias for {@link #write(File)}.</p>
     *
     * @param file the target file
     * @throws JAXBException on serialization error
     */
    public void save(File file) throws JAXBException {
        write(file);
    }

    /**
     * Loads a calendar file and makes it active.
     * <p>
     * If the file does not exist or is empty, it is created and the
     * calendar is initialized with an empty event collection. In all
     * other cases, the contents are read and applied to the
     * {@link Calendar}.
     * </p>
     *
     * @param file the file to load
     * @throws FileNotFoundException if the file cannot be opened
     * @throws JAXBException on deserialization error
     */
    @Override
    public void load(File file) throws FileNotFoundException, JAXBException {
        try {
            if (!file.exists() || file.length() == 0) {
                file.createNewFile();
                Calendar.getInstance().setEvents(new ArrayList<>());
                this.file = file;
                return;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        read(file);
        this.file = file;
    }

    /**
     * Writes the current state back to the active file.
     * <p>
     * If there is no currently opened file, a message is printed and
     * the operation completes without error.
     * </p>
     *
     * @throws JAXBException on serialization error
     */
    @Override
    public void unload() throws JAXBException {
        if (file == null) {
            System.out.println("No file open.");
            return;
        }

        write(file);
    }

    /**
     * Returns a reference to the currently opened file.
     *
     * @return the active file, or {@code null} if none is open
     */
    public File getFile() {
        return file;
    }

    /**
     * Closes the current file without persisting any changes:
     * clears the calendar's event list and forgets the file reference.
     */
    public void closeFile() {
        Calendar.getInstance().setEvents(new ArrayList<>());
        this.file = null;
    }
}
