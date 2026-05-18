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

/**
 * Command that finds the earliest free time slot of at least a given
 * duration on a specific date.
 * <p>
 * Usage: <code>findslot &lt;date&gt; &lt;hours&gt;</code>. The duration
 * argument can be expressed as plain hours (e.g. <code>2</code>) or as
 * "hours:minutes" (e.g. <code>1:30</code>).
 * </p>
 * <p>
 * Free slots are searched within the working day - from
 * {@link #WORK_START} (08:00) to {@link #WORK_END} (17:00) - and only
 * on working days (dates not marked as {@code HOLIDAY}).
 * </p>
 *
 * <p>
 * The helper methods {@link #findSlot(List, LocalDate, Duration)},
 * {@link #isHoliday(List, LocalDate)} and {@link #parseDuration(String)}
 * are declared {@code public static} so they can be reused from
 * {@link FindSlotWith}.
 * </p>
 */
public class FindSlot implements Executable {

    /** Start of the working day. */
    public static final LocalTime WORK_START = LocalTime.of(8, 0);

    /** End of the working day. */
    public static final LocalTime WORK_END = LocalTime.of(17, 0);

    /** Arguments passed to the command from user input. */
    private final List<String> arguments;

    /**
     * Constructs a new {@code findslot} command with the given arguments.
     *
     * @param arguments argument list (exactly 2 are expected - date and
     *                  duration)
     */
    public FindSlot(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Executes the {@code findslot} operation.
     * <p>
     * Delegates the search to the static
     * {@link #findSlot(List, LocalDate, Duration)} method and prints the
     * result.
     * </p>
     */
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

    /**
     * Finds the earliest free slot in the given set of events for the
     * specified date and duration.
     * <p>
     * The algorithm starts from {@link #WORK_START}, walks through the
     * day's events sorted by start time, and checks whether there is
     * enough free room before each event. When there is not, the
     * "current position" is moved past the end of the inspected event.
     * Finally it checks whether enough time remains before
     * {@link #WORK_END}.
     * </p>
     *
     * @param events the full list of events to consider
     * @param date the date for which a slot is sought
     * @param duration the minimum required duration
     * @return the found free slot, or {@code null} if:
     *         <ul>
     *           <li>the input is invalid;</li>
     *           <li>the requested duration exceeds the working day;</li>
     *           <li>the date is a holiday;</li>
     *           <li>not enough contiguous time remains between existing
     *               events.</li>
     *         </ul>
     */
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

    /**
     * Checks whether the given list of events contains a holiday marker
     * for the specified date.
     *
     * @param events the events to inspect
     * @param date the date to check
     * @return {@code true} if the date is marked as {@code HOLIDAY}
     */
    public static boolean isHoliday(List<Event> events, LocalDate date) {
        return events.stream()
                .anyMatch(e -> e.getDate() != null
                        && e.getDate().equals(date)
                        && "HOLIDAY".equalsIgnoreCase(e.getName()));
    }

    /**
     * Parses a duration string into a {@link Duration} object.
     * <p>
     * Two formats are supported:
     * </p>
     * <ul>
     *   <li>hours only, e.g. <code>"2"</code>;</li>
     *   <li>hours and minutes, separated by a colon, e.g.
     *       <code>"1:30"</code>.</li>
     * </ul>
     *
     * @param value textual representation of the duration
     * @return the corresponding duration
     * @throws NumberFormatException if the numeric parts are invalid
     */
    public static Duration parseDuration(String value) {
        if (value.contains(":")) {
            String[] parts = value.split(":");

            return Duration.ofHours(Long.parseLong(parts[0]))
                    .plusMinutes(Long.parseLong(parts[1]));
        }

        return Duration.ofHours(Long.parseLong(value));
    }

    /**
     * Lightweight description of a free time slot in the calendar.
     *
     * @param date date of the slot
     * @param start start time of the slot
     * @param end end time of the slot
     */
    public record Slot(LocalDate date, LocalTime start, LocalTime end) {
    }
}
