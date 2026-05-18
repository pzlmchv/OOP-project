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

/**
 * Command that marks a given date as a non-working day (holiday).
 * <p>
 * Usage: <code>holiday &lt;date&gt;</code>. Technically, it adds a
 * special event named <code>HOLIDAY</code> to the calendar, covering
 * the entire day (00:00:00 - 23:59:59).
 * </p>
 * <p>
 * The command refuses to mark a date if:
 * </p>
 * <ul>
 *   <li>the date is already marked as a holiday; or</li>
 *   <li>the date already has other events - those must be removed
 *       first.</li>
 * </ul>
 *
 * @see Calendar#hasHoliday(LocalDate)
 */
public class Holiday implements Executable {

    /** Arguments passed to the command from user input. */
    private final List<String> arguments;

    /**
     * Constructs a new {@code holiday} command with the given arguments.
     *
     * @param arguments argument list (exactly 1 is expected - the date)
     */
    public Holiday(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Executes the {@code holiday} operation.
     * <p>
     * Builds a special "HOLIDAY" event covering the whole day and adds
     * it to the calendar through {@link Calendar#addEvent(Event)}.
     * </p>
     */
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
