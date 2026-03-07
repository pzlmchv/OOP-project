package bg.tu_varna.ks.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private String name;
    private String note;

    public Event(LocalDate date, LocalTime start, LocalTime end, String name, String note) {
        this.date = date;
        this.start = start;
        this.end = end;
        this.name = name;
        this.note = note;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}