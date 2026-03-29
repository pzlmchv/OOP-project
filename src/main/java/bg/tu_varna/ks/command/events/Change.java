package bg.tu_varna.ks.command.events;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.models.Calendar;
import bg.tu_varna.ks.models.Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Change implements Executable {
    private List<String> arguments;

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
            System.err.println("arguments not right count???");
            return;
        }

        LocalDate date = LocalDate.parse(arguments.get(0));
        LocalTime starttime = LocalTime.parse(arguments.get(1));
        String option = arguments.get(2);
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

        Calendar.getInstance().setOption(date, starttime, option, newValue);

        System.out.println("Changed event successfully");
    }
}
