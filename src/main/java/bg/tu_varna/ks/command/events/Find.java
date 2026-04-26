package bg.tu_varna.ks.command.events;

import bg.tu_varna.ks.command.files.AppData;
import bg.tu_varna.ks.contracts.Executable;
import bg.tu_varna.ks.models.Calendar;
import bg.tu_varna.ks.models.Event;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Find implements Executable {
    private final List<String> arguments;

    public Find(List<String> arguments) {
        this.arguments = arguments;
    }

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

    private boolean contains(String value, String searchedText) {
        return value != null && value.toLowerCase().contains(searchedText);
    }
}
