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

public class Merge implements Executable {
    private final List<String> arguments;
    private static final String PATH = ".\\src\\main\\java\\bg\\tu_varna\\ks\\files\\";

    public Merge(List<String> arguments) {
        this.arguments = arguments;
    }

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

    private Optional<Event> findConflict(Event event) {
        return Calendar.getInstance().getEventsByDate(event.getDate())
                .stream()
                .filter(e -> event.getStart().isBefore(e.getEnd()) && event.getEnd().isAfter(e.getStart()))
                .findFirst();
    }

    private boolean moveImportedEvent(Scanner scanner, Event importedEvent) {
        Event movedImported = readMovedEvent(scanner, importedEvent);
        return Calendar.getInstance().addEvent(movedImported);
    }

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

    private File buildFile(String fileName) {
        File file = new File(fileName);

        if (file.isAbsolute() || fileName.contains("/") || fileName.contains("\\")) {
            return file;
        }

        return new File(PATH.concat(fileName));
    }
}
