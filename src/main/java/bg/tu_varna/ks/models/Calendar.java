package bg.tu_varna.ks.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class Calendar {
    private List<Event> events;
    private Set<LocalDate> holidays;
    private String currentFileName;

    public Calendar() {
        this.events = new ArrayList<>();
        this.holidays = new HashSet<>();
        this.currentFileName = null;
    }

    public boolean addEvent(Event event) {
        if (isTimeSlotAvailable(event.getDate(), event.getStart(), event.getEnd())) {
            events.add(event);
            return true;
        }
        return false;
    }

    public boolean removeEvent(LocalDate date, LocalTime start, LocalTime end) {
        return events.removeIf(e ->
                e.getDate().equals(date) &&
                        e.getStart().equals(start) &&
                        e.getEnd().equals(end)
        );
    }

    public List<Event> getEventsForDate(LocalDate date) {
        return events.stream()
                .filter(e -> e.getDate().equals(date))
                .sorted(Comparator.comparing(Event::getStart))
                .collect(Collectors.toList());
    }

    public boolean isTimeSlotAvailable(LocalDate date, LocalTime start, LocalTime end) {
        if (isHoliday(date)) {
            return false;
        }

        return events.stream()
                .filter(e -> e.getDate().equals(date))
                .noneMatch(e ->
                        (start.isBefore(e.getEnd()) && end.isAfter(e.getStart()))
                );
    }

    public void addHoliday(LocalDate date) {
        holidays.add(date);
    }

    public boolean isHoliday(LocalDate date) {
        return holidays.contains(date);
    }

    public List<Event> findEventsByString(String search) {
        String lowerSearch = search.toLowerCase();
        return events.stream()
                .filter(e ->
                        e.getName().toLowerCase().contains(lowerSearch) ||
                                e.getNote().toLowerCase().contains(lowerSearch)
                )
                .collect(Collectors.toList());
    }

    public void setCurrentFileName(String fileName) {
        this.currentFileName = fileName;
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    // Допълнителни helper методи
    public boolean isWorkingHour(LocalTime time) {
        return !time.isBefore(LocalTime.of(8, 0)) &&
                !time.isAfter(LocalTime.of(17, 0));
    }
}