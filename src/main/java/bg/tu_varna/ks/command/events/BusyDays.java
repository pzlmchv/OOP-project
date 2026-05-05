package bg.tu_varna.ks.command.events;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.models.Calendar;
import bg.tu_varna.ks.models.Event;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BusyDays implements Executable {
    private final List<String> arguments;

    public BusyDays(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute() {
        if (Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("no open file");
            return;
        }

        if (arguments.size() != 2) {
            System.err.println("usage: busydays <from> <to>");
            return;
        }

        LocalDate from = LocalDate.parse(arguments.get(0));
        LocalDate to = LocalDate.parse(arguments.get(1));

        if (from.isAfter(to)) {
            System.err.println("from date must be before or equal to to date");
            return;
        }

        Map<LocalDate, Long> busyMinutes = Calendar.getInstance().getEvents()
                .stream()
                .filter(e -> !"HOLIDAY".equalsIgnoreCase(e.getName()))
                .filter(e -> !e.getDate().isBefore(from) && !e.getDate().isAfter(to))
                .collect(Collectors.groupingBy(
                        Event::getDate,
                        Collectors.summingLong(e -> Duration.between(e.getStart(), e.getEnd()).toMinutes())
                ));

        if (busyMinutes.isEmpty()) {
            System.out.println("No busy days in this period");
            return;
        }

        busyMinutes.entrySet()
                .stream()
                .sorted(Map.Entry.<LocalDate, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(entry -> System.out.printf(
                        "%s - %d hours %d minutes%n",
                        entry.getKey(),
                        entry.getValue() / 60,
                        entry.getValue() % 60
                ));
    }
}
