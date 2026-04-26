package bg.tu_varna.ks.models;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@XmlRootElement(name = "calendar")
public class Calendar {
    private static Calendar instance;
    private List<Event> events;

    private Calendar() {
        this.events = new ArrayList<>();
    }

    public static Calendar getInstance() {
        if (instance == null) {
            instance = new Calendar();
        }
        return instance;
    }

    public List<Event> getEvents() {
        return events;
    }

    @XmlElementWrapper(name = "events")
    @XmlElement(name = "eventList")
    public void setEvents(List<Event> events) {
        this.events = events == null ? new ArrayList<>() : events;
    }

    public boolean isConflictingEvent(Event event) {
        return isConflictingEvent(event, null);
    }

    public boolean isConflictingEvent(Event event, Event ignoredEvent) {
        return getEventsByDate(event.getDate())
                .stream()
                .filter(e -> ignoredEvent == null || !e.equals(ignoredEvent))
                .anyMatch(e -> event.getStart().isBefore(e.getEnd())
                        && event.getEnd().isAfter(e.getStart())
                );
    }

    public boolean addEvent(Event event) {
        if (!isValidEvent(event)) {
            System.err.println("invalid event");
            return false;
        }

        if (isConflictingEvent(event)) {
            System.err.println("conflicting event");
            return false;
        }

        events.add(event);
        return true;
    }

    public boolean canAddEvent(Event event) {
        return isValidEvent(event) && !isConflictingEvent(event);
    }

    public boolean isValidEvent(Event event) {
        return event != null
                && event.getDate() != null
                && event.getStart() != null
                && event.getEnd() != null
                && event.getStart().isBefore(event.getEnd());
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }

    public List<Event> getEventsByDate(LocalDate date) {
        return this.events.stream()
                .filter(e -> e.getDate().equals(date))
                .sorted(Comparator.comparing(Event::getStart))
                .toList();
    }

    public boolean hasHoliday(LocalDate date) {
        return getEventsByDate(date)
                .stream()
                .anyMatch(e -> "HOLIDAY".equalsIgnoreCase(e.getName()));
    }

    public boolean setOption(LocalDate date, LocalTime starttime, String option, String newValue) {
        Optional<Event> event = events
                .stream()
                .filter(e -> e.getDate().equals(date) && e.getStart().equals(starttime))
                .findFirst();

        if (event.isEmpty()) {
            System.err.println("no event found");
            return false;
        }

        Event eventToChange = event.get();
        LocalDate oldDate = eventToChange.getDate();
        LocalTime oldStart = eventToChange.getStart();
        LocalTime oldEnd = eventToChange.getEnd();
        String oldName = eventToChange.getName();
        String oldNote = eventToChange.getNote();

        switch (option) {
            case "date" -> eventToChange.setDate(LocalDate.parse(newValue));
            case "starttime" -> eventToChange.setStart(LocalTime.parse(newValue));
            case "endtime" -> eventToChange.setEnd(LocalTime.parse(newValue));
            case "name" -> eventToChange.setName(newValue);
            case "note" -> eventToChange.setNote(newValue);
            default -> {
                System.err.println("unknown option");
                return false;
            }
        }

        if (!isValidEvent(eventToChange) || isConflictingEvent(eventToChange, eventToChange)) {
            System.err.println("conflicting or invalid event");
            eventToChange.setDate(oldDate);
            eventToChange.setStart(oldStart);
            eventToChange.setEnd(oldEnd);
            eventToChange.setName(oldName);
            eventToChange.setNote(oldNote);
            return false;
        }

        return true;
    }
}
