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
import java.util.List;

public class AppData implements FileHandler {
    private static AppData instance;
    private File file;
    private JAXBContext context;

    private AppData() {
    }

    public static AppData getInstance() {
        if (instance == null) {
            instance = new AppData();
        }
        return instance;
    }

    public void write(File file) throws JAXBException {
        context = JAXBContext.newInstance(Calendar.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(Calendar.getInstance(), file);
    }

    public void read(File file) throws JAXBException, FileNotFoundException {
        context = JAXBContext.newInstance(Calendar.class);
        List<Event> events = (
                (Calendar)context.createUnmarshaller()
                        .unmarshal(
                                new FileReader(
                                        file.getAbsolutePath()
                                )
                        )
        ).getEvents();
        Calendar.getInstance().setEvents(events);
    }

    public void save(File file) throws JAXBException {
        write(file);
    }

    @Override
    public void load(File file) throws FileNotFoundException, JAXBException {
        try {
            if (!file.exists() || file.length() == 0) {
                file.createNewFile();
                this.file = file;
                return;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        read(file);
        this.file = file;
    }

    @Override
    public void unload() throws JAXBException {
        if (file == null) {
            System.out.println("No file open.");
        }

        write(file);
    }

    public File getFile() {
        return file;
    }

    public void closeFile() {
        this.file = null;
    }
}
