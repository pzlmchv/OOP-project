package bg.tu_varna.ks.command.events;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.models.Calendar;
import bg.tu_varna.ks.models.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Command that prints all events scheduled for a given date, in
 * chronological order.
 * <p>
 * Usage: <code>agenda &lt;date&gt;</code>. The expected date format is
 * <code>YYYY-MM-DD</code>. Events are sorted by start time through
 * {@link Calendar#getEventsByDate(LocalDate)}.
 * </p>
 *
 * @see Calendar#getEventsByDate(LocalDate)
 */
public class Agenda implements Executable {

    /** Arguments passed to the command from user input. */
    private List<String> arguments;

    /**
     * Constructs a new {@code agenda} command with the given arguments.
     *
     * @param arguments argument list (exactly 1 is expected - the date)
     */
    public Agenda(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Executes the {@code agenda} operation.
     * <p>
     * Fetches all events for the given date and prints them one after
     * the other, separated by an empty line for readability.
     * </p>
     */
    @Override
    public void execute() {
        if (Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("no open file");
            return;
        }

        if (arguments.size() != 1) {
            System.err.println("arguments not right count???");
            return;
        }

        LocalDate date = LocalDate.parse(arguments.get(0));
        System.out.println("Agenda for ".concat(date.toString()));

        List<Event> events = Calendar.getInstance().getEventsByDate(date);

        for (int i = 0; i < events.size(); i++) {
            System.out.println((i == events.size() - 1) ? events.get(i) : events.get(i).toString().concat("\n"));
        }
    }
}
