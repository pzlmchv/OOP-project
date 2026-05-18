package bg.tu_varna.ks.command.events;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.models.Calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * Command that modifies a single field of an existing event.
 * <p>
 * Usage:
 * <code>change &lt;date&gt; &lt;starttime&gt; &lt;option&gt; &lt;newvalue&gt;</code>.
 * The pair <i>date + start time</i> identifies the event, and
 * {@code option} is one of:
 * </p>
 * <ul>
 *   <li>{@code date} - new date (format <code>YYYY-MM-DD</code>);</li>
 *   <li>{@code starttime} - new start time (<code>HH:mm</code>);</li>
 *   <li>{@code endtime} - new end time (<code>HH:mm</code>);</li>
 *   <li>{@code name} - new name;</li>
 *   <li>{@code note} - new note.</li>
 * </ul>
 * <p>
 * If the change leaves the event invalid or causes a conflict with
 * another event, it is automatically rolled back.
 * </p>
 *
 * @see Calendar#setOption(LocalDate, LocalTime, String, String)
 */
public class Change implements Executable {

    /** Arguments passed to the command from user input. */
    private final List<String> arguments;

    /**
     * Constructs a new {@code change} command with the given arguments.
     *
     * @param arguments argument list (exactly 4 are expected)
     */
    public Change(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Executes the {@code change} operation.
     * <p>
     * Validates the argument count and the field name, then delegates
     * the actual modification to
     * {@link Calendar#setOption(LocalDate, LocalTime, String, String)}.
     * </p>
     */
    @Override
    public void execute() {
        if (Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("no open file");
            return;
        }

        if (arguments.size() != 4) {
            System.err.println("usage: change <date> <starttime> <option> <newvalue>");
            return;
        }

        LocalDate date = LocalDate.parse(arguments.get(0));
        LocalTime starttime = LocalTime.parse(arguments.get(1));
        String option = arguments.get(2).toLowerCase();
        String newValue = arguments.get(3);

        if (!option.equals("date")
                && !option.equals("starttime")
                && !option.equals("endtime")
                && !option.equals("name")
                && !option.equals("note")
        ) {
            System.err.println("unknown option. valid options: date, starttime, endtime, name or note");
            return;
        }

        if (Calendar.getInstance().setOption(date, starttime, option, newValue)) {
            System.out.println("Changed event successfully");
        }
    }
}
