package bg.tu_varna.ks.command.files.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;


    @Override
    public LocalDate unmarshal(String s) throws Exception {
        return (s == null || s.isEmpty()) ? null : LocalDate.parse(s, FORMATTER);
    }

    @Override
    public String marshal(LocalDate localDate) throws Exception {
        return (localDate == null) ? null : localDate.format(FORMATTER);
    }
}
