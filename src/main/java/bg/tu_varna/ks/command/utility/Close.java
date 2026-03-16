package bg.tu_varna.ks.command.utility;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;

import java.util.Objects;

public class Close implements Executable {
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
