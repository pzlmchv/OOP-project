/**
 * JAXB adapters for the types {@link java.time.LocalDate} and
 * {@link java.time.LocalTime}.
 * <p>
 * Since JAXB does not directly recognize the types from
 * <code>java.time</code>, these adapters perform the conversion between
 * ISO-formatted strings and the corresponding date/time objects. They
 * are applied through the
 * {@link jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter}
 * annotation on the relevant fields and setters in the
 * {@link bg.tu_varna.ks.models.Event} model.
 * </p>
 */
package bg.tu_varna.ks.command.files.adapters;
