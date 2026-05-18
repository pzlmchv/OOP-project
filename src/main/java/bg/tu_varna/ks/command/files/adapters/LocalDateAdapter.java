package bg.tu_varna.ks.command.files.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * JAXB adapter that converts between an ISO-formatted string
 * (<code>YYYY-MM-DD</code>) and a {@link LocalDate}.
 * <p>
 * It is applied through the
 * {@link jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter}
 * annotation on fields or setters of type {@code LocalDate}, because
 * JAXB does not directly recognize the <code>java.time</code> types.
 * </p>
 *
 * @see jakarta.xml.bind.annotation.adapters.XmlAdapter
 * @see LocalTimeAdapter
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    /** Formatter for ISO dates (<code>YYYY-MM-DD</code>). */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Converts a string into a {@link LocalDate}.
     *
     * @param s ISO-formatted input string; may be {@code null} or empty
     * @return the corresponding date, or {@code null} if the input was empty
     * @throws Exception on invalid format
     */
    @Override
    public LocalDate unmarshal(String s) throws Exception {
        return (s == null || s.isEmpty()) ? null : LocalDate.parse(s, FORMATTER);
    }

    /**
     * Converts a {@link LocalDate} into a string for writing to XML.
     *
     * @param localDate the date to be serialized; may be {@code null}
     * @return the textual ISO representation, or {@code null} if the input
     *         was {@code null}
     * @throws Exception on formatting failure
     */
    @Override
    public String marshal(LocalDate localDate) throws Exception {
        return (localDate == null) ? null : localDate.format(FORMATTER);
    }
}
