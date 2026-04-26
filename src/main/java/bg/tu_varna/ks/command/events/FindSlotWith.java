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

public class FindSlotWith implements Executable {
    private final List<String> arguments;
    private static final String PATH = ".\\src\\main\\java\\bg\\tu_varna\\ks\\files\\";

    public FindSlotWith(List<String> arguments) {
        this.arguments = arguments;
    }

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

    private File buildFile(String fileName) {
        File file = new File(fileName);

        if (file.isAbsolute() || fileName.contains("/") || fileName.contains("\\")) {
            return file;
        }

        return new File(PATH.concat(fileName));
    }
}
