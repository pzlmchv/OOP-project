package bg.tu_varna.ks.command.utility;

import bg.tu_varna.ks.contracts.Executable;

public class Exit implements Executable {
    @Override
    public void execute() {
        System.out.println("Exiting app!");
        System.exit(0);
    }
}
