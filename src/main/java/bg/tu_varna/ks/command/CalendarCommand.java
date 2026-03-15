package bg.tu_varna.ks.command;

import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.models.Calendar;
import java.util.List;

public abstract class CalendarCommand implements Executable {
    protected Calendar calendar;
    protected List<String> arguments;

    public CalendarCommand(Calendar calendar, List<String> arguments) {
        this.calendar = calendar;
        this.arguments = arguments;
    }

    protected boolean validateArguments(int expectedCount) {
        return arguments.size() >= expectedCount;
    }

    protected void printError(String message) {
        System.out.println("Error: " + message);
    }
}