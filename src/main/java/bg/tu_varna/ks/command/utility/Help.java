package bg.tu_varna.ks.command.utility;

import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.command.Command;

public class Help implements Executable {
    @Override
    public void execute() {
        for (Command command : Command.values()) {
            System.out.printf("%-20s%s\n", command.getName().toUpperCase(), command.getDescription());
        }
    }
}
