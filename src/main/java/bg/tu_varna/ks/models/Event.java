package bg.tu_varna.ks.models;

import bg.tu_varna.ks.command.files.adapters.LocalDateAdapter;
import bg.tu_varna.ks.command.files.adapters.LocalTimeAdapter;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Domain model for a single event (meeting) in the calendar.
 * <p>
 * Every event has a unique {@link UUID} identifier, a date, start and
 * end time, a name, and an optional note. The class is annotated with
 * JAXB annotations so that it can be automatically serialized to/from
 * an XML file via {@link bg.tu_varna.ks.command.files.AppData}.
 * </p>
 *
 * <p>
 * The recommended way to create new instances is to use the nested
 * {@link EventBuilder} class (the <b>Builder</b> pattern), which provides
 * readable and orderly initialization code:
 * </p>
 * <pre>{@code
 * Event event = new Event.EventBuilder()
 *         .id(UUID.randomUUID())
 *         .date(LocalDate.of(2025, 1, 15))
 *         .start(LocalTime.of(9, 0))
 *         .end(LocalTime.of(10, 0))
 *         .name("Team meeting")
 *         .note("discuss roadmap")
 *         .build();
 * }</pre>
 *
 * @see Calendar
 * @see EventBuilder
 */
@XmlRootElement(name = "event")
@XmlType(propOrder = {"id", "date", "start", "end", "name", "note"})
public class Event {

    /** Unique identifier of the event. */
    private UUID id;

    /** Date on which the event is scheduled. */
    private LocalDate date;

    /** Start time of the event. */
    private LocalTime start;

    /** End time of the event. */
    private LocalTime end;

    /** Short name/title of the event. */
    private String name;

    /** Optional note attached to the event; may be an empty string. */
    private String note;

    /**
     * Private constructor used by {@link EventBuilder#build()}.
     *
     * @param builder the builder from which to extract values
     */
    private Event(EventBuilder builder) {
        this.id = builder.id;
        this.date = builder.date;
        this.start = builder.start;
        this.end = builder.end;
        this.name = builder.name;
        this.note = builder.note;
    }

    /**
     * No-argument constructor required by JAXB for deserialization.
     */
    public Event() {}

    /**
     * Returns the unique identifier of the event.
     *
     * @return the event's UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the event.
     * <p>Serialized as an XML attribute.</p>
     *
     * @param id the new identifier
     */
    @XmlAttribute
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Returns the event's date.
     *
     * @return the event date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the event's date.
     * <p>Serialized via {@link LocalDateAdapter}.</p>
     *
     * @param date the new date
     */
    @XmlElement(name = "date")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the event's start time.
     *
     * @return the start time
     */
    public LocalTime getStart() {
        return start;
    }

    /**
     * Sets the event's start time.
     * <p>Serialized via {@link LocalTimeAdapter}.</p>
     *
     * @param start the new start time
     */
    @XmlElement(name = "start")
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    public void setStart(LocalTime start) {
        this.start = start;
    }

    /**
     * Returns the event's end time.
     *
     * @return the end time
     */
    public LocalTime getEnd() {
        return end;
    }

    /**
     * Sets the event's end time.
     * <p>Serialized via {@link LocalTimeAdapter}.</p>
     *
     * @param end the new end time
     */
    @XmlElement(name = "end")
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    public void setEnd(LocalTime end) {
        this.end = end;
    }

    /**
     * Returns the name of the event.
     *
     * @return the event name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the event.
     *
     * @param name the new name
     */
    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the note associated with the event.
     *
     * @return the note; may be an empty string
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets the note of the event.
     *
     * @param note the new note
     */
    @XmlElement(name = "note")
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Returns a human-readable string representation of the event,
     * including date, time interval, name and (if present) note.
     *
     * @return a multi-line representation of the event
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        return sb.append("Date: ")
                .append(this.date)
                .append("\n")
                .append(this.start)
                .append(" - ")
                .append(this.end)
                .append("\nName: ")
                .append(this.name)
                .append(this.note.isEmpty() ? "" : "\nNote: ")
                .append(this.note.isEmpty() ? "" : this.note)
                .toString();
    }

    /**
     * Builder for {@link Event} objects.
     * <p>
     * Implements the <b>Builder</b> pattern - it allows step-by-step
     * field assignment through chained method calls (<i>fluent API</i>)
     * and finally produces a ready, construction-immutable object via
     * {@link #build()}.
     * </p>
     */
    public static class EventBuilder {

        /** Identifier to be assigned to the new event. */
        private UUID id;

        /** Date of the future event. */
        private LocalDate date;

        /** Start time of the future event. */
        private LocalTime start;

        /** End time of the future event. */
        private LocalTime end;

        /** Name of the future event. */
        private String name;

        /** Note for the future event. */
        private String note;

        /** Creates a new empty builder. */
        public EventBuilder() {}

        /**
         * Sets the identifier of the future event.
         *
         * @param id a unique UUID
         * @return this builder, for chaining
         */
        public EventBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the date of the future event.
         *
         * @param date the event date
         * @return this builder, for chaining
         */
        public EventBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }

        /**
         * Sets the start time of the future event.
         *
         * @param start the start time
         * @return this builder, for chaining
         */
        public EventBuilder start(LocalTime start) {
            this.start = start;
            return this;
        }

        /**
         * Sets the end time of the future event.
         *
         * @param end the end time
         * @return this builder, for chaining
         */
        public EventBuilder end(LocalTime end) {
            this.end = end;
            return this;
        }

        /**
         * Sets the name of the future event.
         *
         * @param name the event name
         * @return this builder, for chaining
         */
        public EventBuilder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the note of the future event.
         *
         * @param note the note
         * @return this builder, for chaining
         */
        public EventBuilder note(String note) {
            this.note = note;
            return this;
        }

        /**
         * Builds a new {@link Event} object from the current builder
         * values.
         *
         * @return the new event
         */
        public Event build() {
            return new Event(this);
        }
    }
}
