package bg.tu_varna.ks.models;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.util.ArrayList;
import java.util.List;

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

    public void addEvent(Event event) {
        events.add(event);
    }
}
