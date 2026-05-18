package bg.tu_varna.ks.command;

/**
 * Enumeration describing all supported commands in the calendar
 * information system.
 * <p>
 * Each value carries a pair of strings: {@code name} - the name as the
 * user types it on the console (case-insensitive), and {@code description}
 * - a short description shown by the
 * {@link bg.tu_varna.ks.command.utility.Help} command.
 * </p>
 *
 * <p>Two groups of commands are supported:</p>
 * <ul>
 *   <li><b>Utility commands</b> - {@link #OPEN}, {@link #CLOSE},
 *       {@link #SAVE}, {@link #SAVEAS}, {@link #HELP}, {@link #EXIT}.</li>
 *   <li><b>Event commands</b> - {@link #BOOK}, {@link #UNBOOK},
 *       {@link #AGENDA}, {@link #CHANGE}, {@link #FIND}, {@link #HOLIDAY},
 *       {@link #BUSYDAYS}, {@link #FINDSLOT}, {@link #FINDSLOTWITH},
 *       {@link #MERGE}.</li>
 * </ul>
 *
 * @see CommandRunner
 * @see bg.tu_varna.ks.command.factories.CommandFactory
 */
public enum Command {

    /** Opens a file and loads its contents. */
    OPEN("open", "Opens a file and reads its contents."),

    /** Closes the currently opened file. */
    CLOSE ("close", "Closes the current file."),

    /** Saves the contents to the currently opened file. */
    SAVE("save", "Saves the contents in the current file."),

    /** Saves the contents to a file chosen by the user. */
    SAVEAS("saveas", "Saves the contents in a directory, chosen by the user."),

    /** Displays the list of all available commands. */
    HELP("help", "Shows all possible commands."),

    /** Terminates the application. */
    EXIT("exit", "Exits the application."),

    /** Schedules a meeting with a name and a note at a given date and time. */
    BOOK ("book", "Schedules a meeting with name and note at a given date and time."),

    /** Cancels a meeting at the specified date and time. */
    UNBOOK ("unbook", "Cancels a meeting at the specified date and time."),

    /** Shows all meetings for a given date in chronological order. */
    AGENDA ("agenda", "Shows all meetings for the given date in chronological order."),

    /** Changes a property of an existing meeting (date, time, name, or note). */
    CHANGE ("change", "Changes a property of an existing meeting (date, time, name or note)."),

    /** Searches meetings by name or note. */
    FIND ("find", "Searches meetings by name or note."),

    /** Marks a date as a non-working day (holiday). */
    HOLIDAY ("holiday", "Marks a date as a non-working day."),

    /** Shows workload statistics between two dates. */
    BUSYDAYS ("busydays", "Shows workload statistics between two dates."),

    /** Finds the earliest available meeting slot of a given duration. */
    FINDSLOT ("findslot", "Finds the earliest available meeting slot."),

    /** Finds a free slot synchronized with another calendar. */
    FINDSLOTWITH ("findslotwith", "Finds a free slot synchronized with another calendar."),

    /** Merges events from an external calendar file with the current calendar. */
    MERGE ("merge", "Merges events from another calendar file.");

    /** The name of the command as typed by the user. */
    private final String name;

    /** Short description of the command, shown by {@code help}. */
    private final String description;

    /**
     * Constructs an enum value with the given name and description.
     *
     * @param name the command name
     * @param description the description displayed by help
     */
    Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Returns the command name (lowercase).
     *
     * @return the command name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the command description.
     *
     * @return the command description
     */
    public String getDescription() {
        return description;
    }
}
