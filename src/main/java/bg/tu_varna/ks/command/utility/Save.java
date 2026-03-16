package bg.tu_varna.ks.command.utility;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;

import jakarta.xml.bind.JAXBException;
import java.util.Objects;

public class Save implements Executable {
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
