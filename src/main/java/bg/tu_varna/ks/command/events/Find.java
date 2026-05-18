package bg.tu_varna.ks.command.events;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.models.Calendar;
import bg.tu_varna.ks.models.Event;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Command that searches for events whose name or note contains a given
 * substring.
 * <p>
 * Usage: <code>find &lt;string&gt;</code>. The search is
 * case-insensitive. The results are sorted first by date and then by
 * start time.
 * </p>
 */
public class Find implements Executable {

    /** Arguments passed to the command from user input. */
    private final List<String> arguments;

    /**
     * Constructs a new {@code find} command with the given arguments.
     *
     * @param arguments argument list (exactly 1 is expected - the search
     *                  substring)
     */
    public Find(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Executes the {@code find} operation.
     * <p>
     * Iterates over the current calendar's events and prints those
     * whose name or note contains the given substring.
     * </p>
     */
    @Override
    public void execute() {
        if (Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("no open file");
            return;
        }

        if (arguments.size() != 1) {
            System.err.println("usage: find <string>");
            return;
        }

        String searchedText = arguments.get(0).toLowerCase();

        List<Event> foundEvents = Calendar.getInstance().getEvents()
                .stream()
                .filter(e -> contains(e.getName(), searchedText) || contains(e.getNote(), searchedText))
                .sorted(Comparator.comparing(Event::getDate).thenComparing(Event::getStart))
                .toList();

        if (foundEvents.isEmpty()) {
            System.out.println("No events found");
            return;
        }

        foundEvents.forEach(e -> System.out.println(e + "\n"));
    }

    /**
     * Helper method that checks whether a given string contains the
     * searched substring (case-insensitively) without throwing on a
     * {@code null} input.
     *
     * @param value the string to search in; may be {@code null}
     * @param searchedText the substring to look for (already lowercased)
     * @return {@code true} if {@code value} is non-null and contains
     *         {@code searchedText}
     */
    private boolean contains(String value, String searchedText) {
        return value != null && value.toLowerCase().contains(searchedText);
    }
}
