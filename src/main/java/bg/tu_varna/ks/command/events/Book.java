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
 * Command that books (schedules) a new event in the currently opened
 * calendar.
 * <p>
 * Usage:
 * <code>book &lt;date&gt; &lt;starttime&gt; &lt;endtime&gt; &lt;name&gt; &lt;note&gt;</code>.
 * Expected formats: <code>YYYY-MM-DD</code> for the date and
 * <code>HH:mm</code> for the times. Name and note, which may contain
 * whitespace, must be enclosed in double quotes.
 * </p>
 * <p>
 * The command automatically checks that the date is not marked as a
 * holiday and that the new event does not overlap an existing one - if
 * either condition is violated, the event is not added.
 * </p>
 *
 * @see Calendar#addEvent(Event)
 * @see Event.EventBuilder
 */
public class Book implements Executable {

    /** Arguments passed to the command from user input. */
    private final List<String> arguments;

    /**
     * Constructs a new {@code book} command with the given arguments.
     *
     * @param arguments argument list (exactly 5 are expected)
     */
    public Book(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Executes the {@code book} operation.
     * <p>
     * Steps: check for an open file, validate argument count, construct
     * an {@link Event} through {@link Event.EventBuilder}, perform a
     * holiday check, and finally add the event to the calendar.
     * </p>
     */
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
