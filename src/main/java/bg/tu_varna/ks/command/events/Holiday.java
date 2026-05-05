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

public class Holiday implements Executable {
    private final List<String> arguments;

    public Holiday(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute() {
        if (Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("no open file");
            return;
        }

        if (arguments.size() != 1) {
            System.err.println("usage: holiday <date>");
            return;
        }

        LocalDate date = LocalDate.parse(arguments.get(0));

        if (Calendar.getInstance().hasHoliday(date)) {
            System.err.println("date is already marked as holiday");
            return;
        }

        if (!Calendar.getInstance().getEventsByDate(date).isEmpty()) {
            System.err.println("cannot mark date as holiday because it already has events");
            return;
        }

        Event holiday = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .date(date)
                .start(LocalTime.MIN)
                .end(LocalTime.of(23, 59, 59))
                .name("HOLIDAY")
                .note("Non-working day")
                .build();

        if (Calendar.getInstance().addEvent(holiday)) {
            System.out.println("Date marked as holiday");
        }
    }
}
