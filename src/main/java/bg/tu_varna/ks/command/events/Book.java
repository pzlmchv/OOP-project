package bg.tu_varna.ks.command.events;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.models.Calendar;
import bg.tu_varna.ks.models.Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Book implements Executable {
    private final List<String> arguments;

    public Book(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute() {
        if (Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("no open file");
            return;
        }

        if (arguments.size() != 5) {
            System.err.println("usage: book <date> <starttime> <endtime> <name> <note>");
            return;
        }

        Event event = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .date(LocalDate.parse(arguments.get(0)))
                .start(LocalTime.parse(arguments.get(1)))
                .end(LocalTime.parse(arguments.get(2)))
                .name(arguments.get(3))
                .note(arguments.get(4))
                .build();

        if (Calendar.getInstance().hasHoliday(event.getDate())) {
            System.err.println("cannot book event on holiday");
            return;
        }

        if (Calendar.getInstance().addEvent(event)) {
            System.out.println("event added successfully");
        }
    }
}
