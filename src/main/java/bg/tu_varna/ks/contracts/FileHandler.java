package bg.tu_varna.ks.contracts;

import jakarta.xml.bind.JAXBException;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Contract for an object that manages loading and saving data to/from
 * a file.
 * <p>
 * Implementations are responsible for the bidirectional interaction with
 * an external storage medium: reading the contents of a file and turning
 * them into the internal application model
 * ({@link bg.tu_varna.ks.models.Calendar}) and vice versa - serializing
 * the current state back to the file.
 * </p>
 *
 * @see bg.tu_varna.ks.command.files.AppData
 */
public interface FileHandler {

    /**
     * Loads the contents of the given file into the current application
     * state.
     *
     * @param file the file to be loaded; must not be {@code null}
     * @throws FileNotFoundException if the file cannot be found
     * @throws JAXBException if an XML deserialization error occurs
     */
    void load(File file) throws FileNotFoundException, JAXBException;

    /**
     * Writes (flushes) the current application state back to the
     * associated file.
     *
     * @throws JAXBException if an XML serialization error occurs
     */
    void unload() throws JAXBException;
}
