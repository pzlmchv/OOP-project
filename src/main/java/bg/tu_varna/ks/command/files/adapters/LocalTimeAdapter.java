package bg.tu_varna.ks.command.files.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public LocalTime unmarshal(String s) throws Exception {
        return (s == null || s.isEmpty()) ? null : LocalTime.parse(s, FORMATTER);
    }

    @Override
    public String marshal(LocalTime localTime) throws Exception {
        return (localTime == null) ? null : localTime.format(FORMATTER);
    }
}
