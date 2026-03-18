package bg.tu_varna.ks.command;

public enum Command {
    OPEN("open", "Opens a file and reads its contents."),
    CLOSE ("close", "Closes the current file."),
    SAVE("save", "Saves the contents in the current file."),
    SAVEAS("saveas", "Saves the contents in a directory, chosen by the user."),
    HELP("help", "Shows all possible commands."),
    EXIT("exit", "Exits the application."),
    BOOK ("book", "Schedules a meeting with name and note at a given date and time."),
    UNBOOK ("unbook", "Cancels a meeting at the specified date and time."),
    AGENDA ("agenda", "Shows all meetings for the given date in chronological order."),
    CHANGE ("change", "Changes a property of an existing meeting (date, time, name or note)."),
    FIND ("find", "Searches meetings by name or note."),
    HOLIDAY ("holiday", "Marks a date as a non-working day."),
    BUSYDAYS ("busydays", "Shows workload statistics between two dates."),
    FINDSLOT ("findslot", "Finds the earliest available meeting slot."),
    FINDSLOTWITH ("findslotwith", "Finds a free slot synchronized with another calendar."),
    MERGE ("merge", "Merges events from another calendar file.");

    private final String name;
    private final String description;

    Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
