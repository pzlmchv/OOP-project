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

/**
 * Command that cancels (removes) an existing event from the calendar.
 * <p>
 * Usage:
 * <code>unbook &lt;date&gt; &lt;starttime&gt; &lt;endtime&gt;</code>.
 * The event is uniquely identified by the triple
 * <i>date + start time + end time</i>. If no such event exists, an
 * error is printed.
 * </p>
 *
 * @see Calendar#removeEvent(Event)
 */
public class Unbook implements Executable {

    /** Arguments passed to the command from user input. */
    private List<String> arguments;

    /**
     * Constructs a new {@code unbook} command with the given arguments.
     *
     * @param arguments argument list (exactly 3 are expected)
     */
    public Unbook(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Executes the {@code unbook} operation.
     * <p>
     * Finds the event by date and time interval and removes it from
     * the {@link Calendar}'s event list.
     * </p>
     */
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
            System.err.println("No event with such name");
            return;
        }

        Calendar.getInstance().removeEvent(event.get());
    }
}
