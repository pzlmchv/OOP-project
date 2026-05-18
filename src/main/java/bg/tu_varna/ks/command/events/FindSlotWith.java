package bg.tu_varna.ks.command.events;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.models.Event;
import jakarta.xml.bind.JAXBException;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Command that finds a free slot common to two calendars (the current
 * one and another one specified as a file).
 * <p>
 * Usage:
 * <code>findslotwith &lt;date&gt; &lt;hours&gt; &lt;calendar&gt;</code>.
 * The implementation merges the events from both calendars into a
 * single combined list and reuses the logic of
 * {@link FindSlot#findSlot(List, LocalDate, Duration)}.
 * </p>
 *
 * @see FindSlot
 */
public class FindSlotWith implements Executable {

    /** Arguments passed to the command from user input. */
    private final List<String> arguments;

    /** Default directory used when looking up files by bare name. */
    private static final String PATH = ".\\src\\main\\java\\bg\\tu_varna\\ks\\files\\";

    /**
     * Constructs a new {@code findslotwith} command with the given
     * arguments.
     *
     * @param arguments argument list (exactly 3 are expected - date,
     *                  duration, and the file name/path of the other
     *                  calendar)
     */
    public FindSlotWith(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Executes the {@code findslotwith} operation.
     * <p>
     * Reads the external calendar file via
     * {@link AppData#readCalendar(File)}, merges its events with those
     * of the active calendar, and searches for a free slot in the
     * resulting union.
     * </p>
     */
    @Override
    public void execute() {
        if (Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("no open file");
            return;
        }

        if (arguments.size() != 3) {
            System.err.println("usage: findslotwith <date> <hours> <calendar>");
            return;
        }

        LocalDate date = LocalDate.parse(arguments.get(0));
        Duration duration = FindSlot.parseDuration(arguments.get(1));
        File calendarFile = buildFile(arguments.get(2));

        try {
            List<Event> combinedEvents = new ArrayList<>();

            combinedEvents.addAll(
                    bg.tu_varna.ks.models.Calendar.getInstance().getEvents()
            );

            combinedEvents.addAll(
                    AppData.getInstance().readCalendar(calendarFile).getEvents()
            );

            FindSlot.Slot slot = FindSlot.findSlot(combinedEvents, date, duration);

            if (slot == null) {
                System.out.println("No common free slot found");
                return;
            }

            System.out.printf(
                    "Common free slot: %s %s - %s%n",
                    slot.date(),
                    slot.start(),
                    slot.end()
            );

        } catch (JAXBException | FileNotFoundException ex) {
            System.err.println("could not read calendar: " + ex.getMessage());
        }
    }

    /**
     * Converts a file name or path string into a {@link File} object.
     * <p>
     * If the given name is already an absolute path or contains
     * separators, it is returned directly. Otherwise, the project's
     * default directory is prepended.
     * </p>
     *
     * @param fileName a file name or path
     * @return the corresponding {@link File} object
     */
    private File buildFile(String fileName) {
        File file = new File(fileName);

        if (file.isAbsolute() || fileName.contains("/") || fileName.contains("\\")) {
            return file;
        }

        return new File(PATH.concat(fileName));
    }
}
