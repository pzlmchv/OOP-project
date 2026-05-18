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

/**
 * Command that prints workload statistics between two dates.
 * <p>
 * Usage: <code>busydays &lt;from&gt; &lt;to&gt;</code>. For every date
 * in the range that has events (excluding those marked as a holiday),
 * the total busy duration is summed and printed in hours and minutes.
 * Results are sorted by descending workload, with date as a tie-breaker.
 * </p>
 *
 * @see Calendar#getEvents()
 */
public class BusyDays implements Executable {

    /** Arguments passed to the command from user input. */
    private final List<String> arguments;

    /**
     * Constructs a new {@code busydays} command with the given arguments.
     *
     * @param arguments argument list (exactly 2 are expected - start and
     *                  end date)
     */
    public BusyDays(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Executes the {@code busydays} operation.
     * <p>
     * Groups events by date with
     * {@link Collectors#groupingBy(java.util.function.Function, java.util.stream.Collector)}
     * and sums their durations in minutes, then formats the output in
     * hours and minutes.
     * </p>
     */
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
