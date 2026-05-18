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

/**
 * Central calendar model - holds all scheduled events from the currently
 * opened file.
 * <p>
 * The class follows the <b>Singleton</b> pattern: there is always exactly
 * one instance, accessible via {@link #getInstance()}. This allows
 * different commands to operate on a shared state without passing it
 * explicitly between them.
 * </p>
 *
 * <p>
 * In addition to storing the event collection, the calendar exposes
 * business-logic operations: event validation, overlap detection,
 * adding/removing events, holiday checks, and modifying individual
 * fields of an event.
 * </p>
 *
 * @see Event
 * @see bg.tu_varna.ks.command.files.AppData
 */
@XmlRootElement(name = "calendar")
public class Calendar {

    /** The single instance of this class (Singleton). */
    private static Calendar instance;

    /** List of all events currently held in the calendar. */
    private List<Event> events;

    /**
     * Private constructor that initializes an empty list of events.
     * Called only from {@link #getInstance()}.
     */
    private Calendar() {
        this.events = new ArrayList<>();
    }

    /**
     * Returns the single instance of the calendar.
     * <p>If one does not yet exist, it is created lazily.</p>
     *
     * @return the singleton instance of {@code Calendar}
     */
    public static Calendar getInstance() {
        if (instance == null) {
            instance = new Calendar();
        }
        return instance;
    }

    /**
     * Returns a direct reference to the list of events.
     * <p>
     * Note: the list is mutable - direct operations on it will bypass
     * the validations performed in {@link #addEvent(Event)}.
     * </p>
     *
     * @return the list of events
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Sets a new list of events (used by JAXB during loading).
     * <p>
     * If {@code null} is passed, a new empty list is stored instead,
     * to avoid {@link NullPointerException} in the rest of the logic.
     * </p>
     *
     * @param events the new list of events; may be {@code null}
     */
    @XmlElementWrapper(name = "events")
    @XmlElement(name = "eventList")
    public void setEvents(List<Event> events) {
        this.events = events == null ? new ArrayList<>() : events;
    }

    /**
     * Checks whether the given event overlaps in time with at least one
     * of the existing events on the same date.
     *
     * @param event the event to check
     * @return {@code true} if there is a conflict; otherwise {@code false}
     */
    public boolean isConflictingEvent(Event event) {
        return isConflictingEvent(event, null);
    }

    /**
     * Checks for overlap, excluding a specific event from the comparison
     * (for example, when editing the same event in place).
     *
     * @param event the event to check
     * @param ignoredEvent an event to be ignored (may be {@code null})
     * @return {@code true} if there is a conflict; otherwise {@code false}
     */
    public boolean isConflictingEvent(Event event, Event ignoredEvent) {
        return getEventsByDate(event.getDate())
                .stream()
                .filter(e -> ignoredEvent == null || !e.equals(ignoredEvent))
                .anyMatch(e -> event.getStart().isBefore(e.getEnd())
                        && event.getEnd().isAfter(e.getStart())
                );
    }

    /**
     * Adds a new event to the calendar after validation and overlap
     * checks.
     *
     * @param event the event to be added
     * @return {@code true} if the event was added successfully;
     *         {@code false} if it is invalid or conflicting
     */
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

    /**
     * Side-effect-free check whether a given event could be added
     * (is valid and conflict-free).
     *
     * @param event the event to check
     * @return {@code true} if the event is valid and does not conflict
     */
    public boolean canAddEvent(Event event) {
        return isValidEvent(event) && !isConflictingEvent(event);
    }

    /**
     * Validates an event against the basic field requirements:
     * non-null date, start and end times, with the start strictly
     * before the end.
     *
     * @param event the event to validate
     * @return {@code true} if all required fields are present and the
     *         time interval is correct
     */
    public boolean isValidEvent(Event event) {
        return event != null
                && event.getDate() != null
                && event.getStart() != null
                && event.getEnd() != null
                && event.getStart().isBefore(event.getEnd());
    }

    /**
     * Removes the specified event from the calendar (if present).
     *
     * @param event the event to be removed
     */
    public void removeEvent(Event event) {
        events.remove(event);
    }

    /**
     * Returns all events for the given date, sorted by start time.
     *
     * @param date the date for which events are requested
     * @return an immutable list of events, ordered chronologically
     */
    public List<Event> getEventsByDate(LocalDate date) {
        return this.events.stream()
                .filter(e -> e.getDate().equals(date))
                .sorted(Comparator.comparing(Event::getStart))
                .toList();
    }

    /**
     * Checks whether the given date is marked as a holiday.
     * <p>
     * A holiday means the date has an event whose name is
     * {@code "HOLIDAY"} (case-insensitive).
     * </p>
     *
     * @param date the date to check
     * @return {@code true} if the date is a holiday
     */
    public boolean hasHoliday(LocalDate date) {
        return getEventsByDate(date)
                .stream()
                .anyMatch(e -> "HOLIDAY".equalsIgnoreCase(e.getName()));
    }

    /**
     * Modifies a single field of an existing event, identified by the
     * <i>date + start time</i> pair.
     * <p>
     * Supported field names are: {@code "date"}, {@code "starttime"},
     * {@code "endtime"}, {@code "name"} and {@code "note"}. If the
     * modification leaves the event invalid or causes a conflict with
     * another event, the change is rolled back and the original values
     * are restored.
     * </p>
     *
     * @param date the date on which the event is scheduled
     * @param starttime the start time of the event
     * @param option the name of the field to be modified
     * @param newValue the new value as a string (parsed according to
     *                 the target field)
     * @return {@code true} on successful change; {@code false} if the
     *         option is unknown, the event is missing, or the change
     *         would produce an invalid/conflicting event
     */
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
