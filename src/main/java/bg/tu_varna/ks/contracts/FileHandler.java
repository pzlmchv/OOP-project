package bg.tu_varna.ks.contracts;

import jakarta.xml.bind.JAXBException;

import java.io.File;
import java.io.FileNotFoundException;

public interface FileHandler {
    void load(File file) throws FileNotFoundException, JAXBException;
    void unload() throws JAXBException;
}
