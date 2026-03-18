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

@XmlRootElement(name = "event")
@XmlType(propOrder = {"id", "date", "start", "end", "name", "note"})
public class Event {
    private UUID id;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private String name;
    private String note;

    private Event(EventBuilder builder) {
        this.id = builder.id;
        this.date = builder.date;
        this.start = builder.start;
        this.end = builder.end;
        this.name = builder.name;
        this.note = builder.note;
    }

    public Event() {}

    public UUID getId() {
        return id;
    }

    @XmlAttribute
    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    @XmlElement(name = "date")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStart() {
        return start;
    }

    @XmlElement(name = "start")
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    @XmlElement(name = "end")
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    @XmlElement(name = "note")
    public void setNote(String note) {
        this.note = note;
    }

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

    public static class EventBuilder {
        private UUID id;
        private LocalDate date;
        private LocalTime start;
        private LocalTime end;
        private String name;
        private String note;

        public EventBuilder() {}

        public EventBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public EventBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public EventBuilder start(LocalTime start) {
            this.start = start;
            return this;
        }

        public EventBuilder end(LocalTime end) {
            this.end = end;
            return this;
        }

        public EventBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EventBuilder note(String note) {
            this.note = note;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }
}