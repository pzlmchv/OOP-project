package bg.tu_varna.ks.command.events;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.models.Event;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class FindSlot implements Executable {
    public static final LocalTime WORK_START = LocalTime.of(8, 0);
    public static final LocalTime WORK_END = LocalTime.of(17, 0);

    private final List<String> arguments;

    public FindSlot(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute() {
        if (Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("no open file");
            return;
        }

        if (arguments.size() != 2) {
            System.err.println("usage: findslot <date> <hours>");
            return;
        }

        LocalDate date = LocalDate.parse(arguments.get(0));
        Duration duration = parseDuration(arguments.get(1));

        Slot slot = findSlot(
                bg.tu_varna.ks.models.Calendar.getInstance().getEvents(),
                date,
                duration
        );

        if (slot == null) {
            System.out.println("No free slot found");
            return;
        }

        System.out.printf(
                "Free slot: %s %s - %s%n",
                slot.date(),
                slot.start(),
                slot.end()
        );
    }

    public static Slot findSlot(List<Event> events, LocalDate date, Duration duration) {
        if (date == null || duration == null || duration.isZero() || duration.isNegative()) {
            return null;
        }

        if (duration.compareTo(Duration.between(WORK_START, WORK_END)) > 0) {
            return null;
        }

        if (isHoliday(events, date)) {
            return null;
        }

        LocalTime possibleStart = WORK_START;

        List<Event> dayEvents = events.stream()
                .filter(e -> e.getDate() != null && e.getDate().equals(date))
                .filter(e -> !"HOLIDAY".equalsIgnoreCase(e.getName()))
                .sorted(Comparator.comparing(Event::getStart))
                .toList();

        for (Event event : dayEvents) {
            if (!possibleStart.plus(duration).isAfter(event.getStart())) {
                return new Slot(date, possibleStart, possibleStart.plus(duration));
            }

            if (event.getEnd().isAfter(possibleStart)) {
                possibleStart = event.getEnd();
            }
        }

        if (!possibleStart.plus(duration).isAfter(WORK_END)) {
            return new Slot(date, possibleStart, possibleStart.plus(duration));
        }

        return null;
    }

    public static boolean isHoliday(List<Event> events, LocalDate date) {
        return events.stream()
                .anyMatch(e -> e.getDate() != null
                        && e.getDate().equals(date)
                        && "HOLIDAY".equalsIgnoreCase(e.getName()));
    }

    public static Duration parseDuration(String value) {
        if (value.contains(":")) {
            String[] parts = value.split(":");

            return Duration.ofHours(Long.parseLong(parts[0]))
                    .plusMinutes(Long.parseLong(parts[1]));
        }

        return Duration.ofHours(Long.parseLong(value));
    }

    public record Slot(LocalDate date, LocalTime start, LocalTime end) {
    }
}
