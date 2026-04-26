package bg.tu_varna.ks.command.events;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.models.Calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class Change implements Executable {
    private final List<String> arguments;

    public Change(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute() {
        if (Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("no open file");
            return;
        }

        if (arguments.size() != 4) {
            System.err.println("usage: change <date> <starttime> <option> <newvalue>");
            return;
        }

        LocalDate date = LocalDate.parse(arguments.get(0));
        LocalTime starttime = LocalTime.parse(arguments.get(1));
        String option = arguments.get(2).toLowerCase();
        String newValue = arguments.get(3);

        if (!option.equals("date")
                && !option.equals("starttime")
                && !option.equals("endtime")
                && !option.equals("name")
                && !option.equals("note")
        ) {
            System.err.println("unknown option. valid options: date, starttime, endtime, name or note");
            return;
        }

        if (Calendar.getInstance().setOption(date, starttime, option, newValue)) {
            System.out.println("Changed event successfully");
        }
    }
}
