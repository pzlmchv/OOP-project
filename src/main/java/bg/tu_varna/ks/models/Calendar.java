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
        this.events = events;
    }

    public Boolean isConflictingEvent(Event event) {
        return getEventsByDate(event.getDate())
                .stream()
                .anyMatch(e -> event.getStart().isBefore(e.getEnd())
                        && event.getEnd().isAfter(e.getStart())
                );
    }

    public void addEvent(Event event) {
        if (isConflictingEvent(event)) {
            System.err.println("conflicting event");
            return;
        }

        events.add(event);
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

    public void setOption(LocalDate date, LocalTime starttime, String option, String newValue) {
        Optional<Event> event = events
                .stream()
                .filter(e -> e.getDate().equals(date) && e.getStart().equals(starttime))
                .findFirst();

        if (event.isEmpty()) {
            System.err.println("no event found");
            return;
        }

        Event eventToChange = event.get();
        LocalDate oldDate = eventToChange.getDate();
        LocalTime oldStart = eventToChange.getStart();
        LocalTime oldEnd = eventToChange.getEnd();
        String oldName = eventToChange.getName();
        String oldNote = eventToChange.getNote();

        switch (option) {
            case "date":
                eventToChange.setDate(LocalDate.parse(newValue));
                break;
            case "starttime":
                eventToChange.setStart(LocalTime.parse(newValue));
                break;
            case "endtime":
                eventToChange.setEnd(LocalTime.parse(newValue));
                break;
            case "name":
                eventToChange.setName(newValue);
                break;
            case "note":
                eventToChange.setNote(newValue);
                break;
        }

        if (isConflictingEvent(eventToChange)) {
            System.err.println("conflicting event");
            eventToChange.setDate(oldDate);
            eventToChange.setStart(oldStart);
            eventToChange.setEnd(oldEnd);
            eventToChange.setName(oldName);
            eventToChange.setNote(oldNote);
        }
    }
}
