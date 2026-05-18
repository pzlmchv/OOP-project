package bg.tu_varna.ks.command.events;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.models.Calendar;
import bg.tu_varna.ks.models.Event;
import jakarta.xml.bind.JAXBException;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

/**
 * Command that merges events from an external calendar file into the
 * current calendar.
 * <p>
 * Usage: <code>merge &lt;calendar&gt;</code>. For every imported event,
 * the algorithm does the following:
 * </p>
 * <ol>
 *   <li>tries to add the event directly to the active calendar;</li>
 *   <li>if a time conflict occurs, informs the user about both colliding
 *       events and asks which one should stay in the slot;</li>
 *   <li>the chosen event keeps the slot, while the other - depending on
 *       the user's choice - is moved to a new date/time that the user
 *       enters interactively.</li>
 * </ol>
 * <p>
 * At the end, the command prints statistics: how many events were
 * added, moved, and skipped.
 * </p>
 *
 * @see Calendar#addEvent(Event)
 */
public class Merge implements Executable {

    /** Arguments passed to the command from user input. */
    private final List<String> arguments;

    /** Default directory used when looking up files by bare name. */
    private static final String PATH = ".\\src\\main\\java\\bg\\tu_varna\\ks\\files\\";

    /**
     * Constructs a new {@code merge} command with the given arguments.
     *
     * @param arguments argument list (exactly 1 is expected - the file
     *                  name or path)
     */
    public Merge(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Executes the {@code merge} operation.
     * <p>
     * Reads the calendar file, iterates over its events, and applies the
     * add-or-resolve-conflict scenario to each. It uses a {@link Scanner}
     * over {@link System#in} for interactive choices from the user.
     * </p>
     */
    @Override
    public void execute() {
        if (Objects.isNull(AppData.getInstance().getFile())) {
            System.err.println("no open file");
            return;
        }

        if (arguments.size() != 1) {
            System.err.println("usage: merge <calendar>");
            return;
        }

        File calendarFile = buildFile(arguments.get(0));

        try {
            List<Event> importedEvents = AppData.getInstance().readCalendar(calendarFile).getEvents();
            int added = 0;
            int moved = 0;
            int skipped = 0;
            Scanner scanner = new Scanner(System.in);

            for (Event importedEvent : importedEvents) {
                if (Calendar.getInstance().addEvent(importedEvent)) {
                    added++;
                    continue;
                }

                Optional<Event> conflict = findConflict(importedEvent);

                if (conflict.isEmpty()) {
                    skipped++;
                    continue;
                }

                System.out.println("Conflict found:");
                System.out.println("1) Current calendar event:");
                System.out.println(conflict.get());
                System.out.println("2) Imported event:");
                System.out.println(importedEvent);
                System.out.print("Choose which event stays in this slot (1 or 2): ");
                String choice = scanner.nextLine().trim();

                if (choice.equals("1")) {
                    if (moveImportedEvent(scanner, importedEvent)) {
                        moved++;
                    } else {
                        skipped++;
                    }
                } else if (choice.equals("2")) {
                    if (keepImportedAndMoveCurrent(scanner, conflict.get(), importedEvent)) {
                        moved++;
                    } else {
                        skipped++;
                    }
                } else {
                    System.err.println("invalid choice. imported event skipped");
                    skipped++;
                }
            }

            System.out.printf("Merge completed. Added: %d, moved: %d, skipped: %d%n", added, moved, skipped);
        } catch (JAXBException | FileNotFoundException ex) {
            System.err.println("could not merge calendar: " + ex.getMessage());
        }
    }

    /**
     * Finds an event in the active calendar that overlaps in time with
     * the given imported event.
     *
     * @param event the imported event
     * @return an {@link Optional} containing the conflicting event, if
     *         any; otherwise an empty {@link Optional}
     */
    private Optional<Event> findConflict(Event event) {
        return Calendar.getInstance().getEventsByDate(event.getDate())
                .stream()
                .filter(e -> event.getStart().isBefore(e.getEnd()) && event.getEnd().isAfter(e.getStart()))
                .findFirst();
    }

    /**
     * Moves the imported event to a new date and/or time entered
     * interactively by the user, and tries to add it to the calendar.
     *
     * @param scanner input stream for user data
     * @param importedEvent the event being moved
     * @return {@code true} if the moved event was successfully added
     */
    private boolean moveImportedEvent(Scanner scanner, Event importedEvent) {
        Event movedImported = readMovedEvent(scanner, importedEvent);
        return Calendar.getInstance().addEvent(movedImported);
    }

    /**
     * Keeps the imported event in the current slot and moves the current
     * event to a new position chosen by the user.
     * <p>
     * On failure (for example, a new conflict), a rollback to the
     * original state is performed.
     * </p>
     *
     * @param scanner input stream for user data
     * @param currentEvent the current event that will be moved
     * @param importedEvent the imported event that will stay in place
     * @return {@code true} if both changes were applied successfully
     */
    private boolean keepImportedAndMoveCurrent(Scanner scanner, Event currentEvent, Event importedEvent) {
        Event movedCurrent = readMovedEvent(scanner, currentEvent);

        Calendar.getInstance().removeEvent(currentEvent);

        if (!Calendar.getInstance().addEvent(importedEvent)) {
            Calendar.getInstance().addEvent(currentEvent);
            return false;
        }

        if (!Calendar.getInstance().addEvent(movedCurrent)) {
            Calendar.getInstance().removeEvent(importedEvent);
            Calendar.getInstance().addEvent(currentEvent);
            return false;
        }

        return true;
    }

    /**
     * Reads a new date, start time, and end time from the user, then
     * builds a new event based on the original but with the updated
     * time data.
     *
     * @param scanner input stream for user data
     * @param original the original event whose name, note, and id are
     *                 preserved
     * @return the newly constructed, relocated event
     */
    private Event readMovedEvent(Scanner scanner, Event original) {
        System.out.println("Enter new date, start time and end time for the moved event.");
        System.out.print("New date [yyyy-mm-dd]: ");
        LocalDate newDate = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("New start time [hh:mm]: ");
        LocalTime newStart = LocalTime.parse(scanner.nextLine().trim());
        System.out.print("New end time [hh:mm]: ");
        LocalTime newEnd = LocalTime.parse(scanner.nextLine().trim());

        return new Event.EventBuilder()
                .id(original.getId())
                .date(newDate)
                .start(newStart)
                .end(newEnd)
                .name(original.getName())
                .note(original.getNote())
                .build();
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
