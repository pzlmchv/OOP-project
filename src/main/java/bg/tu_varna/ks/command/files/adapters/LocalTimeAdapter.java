package bg.tu_varna.ks.command.files.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * JAXB adapter that converts between an ISO-formatted string
 * (<code>HH:mm[:ss]</code>) and a {@link LocalTime}.
 * <p>
 * It is used for {@code LocalTime} fields in the model classes,
 * because JAXB does not directly recognize the
 * <code>java.time</code> types.
 * </p>
 *
 * @see jakarta.xml.bind.annotation.adapters.XmlAdapter
 * @see LocalDateAdapter
 */
public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

    /** Formatter for ISO times (<code>HH:mm[:ss]</code>). */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    /**
     * Converts a string into a {@link LocalTime}.
     *
     * @param s ISO-formatted input string; may be {@code null} or empty
     * @return the corresponding time, or {@code null} if the input was empty
     * @throws Exception on invalid format
     */
    @Override
    public LocalTime unmarshal(String s) throws Exception {
        return (s == null || s.isEmpty()) ? null : LocalTime.parse(s, FORMATTER);
    }

    /**
     * Converts a {@link LocalTime} into a string for writing to XML.
     *
     * @param localTime the time to be serialized; may be {@code null}
     * @return the textual ISO representation, or {@code null} if the input
     *         was {@code null}
     * @throws Exception on formatting failure
     */
    @Override
    public String marshal(LocalTime localTime) throws Exception {
        return (localTime == null) ? null : localTime.format(FORMATTER);
    }
}
