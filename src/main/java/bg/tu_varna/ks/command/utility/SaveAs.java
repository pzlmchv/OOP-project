package bg.tu_varna.ks.command.utility;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;

import jakarta.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

public class SaveAs implements Executable {
    private final List<String> arguments;
    private final String PATH = ".\\src\\main\\java\\bg\\tu_varna\\ks\\files\\";

    public SaveAs(List<String> arguments) {
        this.arguments = arguments;
    }

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
