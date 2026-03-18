package bg.tu_varna.ks.command.events;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.models.Calendar;
import bg.tu_varna.ks.models.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Agenda implements Executable {
    private List<String> arguments;

    public Agenda(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute() {
        if (Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("no open file");
            return;
        }

        if (arguments.size() != 1) {
            System.err.println("arguments not right count???");
            return;
        }

        LocalDate date = LocalDate.parse(arguments.get(0));
        System.out.println("Agenda for ".concat(date.toString()));

        List<Event> events = Calendar.getInstance().getEventsByDate(date);

        for (int i = 0; i < events.size(); i++) {
            System.out.println((i == events.size() - 1) ? events.get(i) : events.get(i).toString().concat("\n"));
        }
    }
}
