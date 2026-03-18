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

public class Unbook implements Executable {
    private List<String> arguments;

    public Unbook(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute() {
        if (Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("no open file");
            return;
        }

        if (arguments.size() != 3) {
            System.err.println("arguments not right count???");
            return;
        }

        LocalDate date = LocalDate.parse(arguments.get(0));
        LocalTime start = LocalTime.parse(arguments.get(1));
        LocalTime end = LocalTime.parse(arguments.get(2));

        Optional<Event> event = Calendar.getInstance()
                .getEvents()
                .stream()
                .filter(e -> e.getDate().equals(date))
                .filter(e -> e.getStart().equals(start))
                .filter(e -> e.getEnd().equals(end))
                .findFirst();

        if (event.isEmpty()) {
            System.err.println("nqma takuv event bonak");
            return;
        }

        Calendar.getInstance().removeEvent(event.get());
    }
}
