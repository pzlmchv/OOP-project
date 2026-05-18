package bg.tu_varna.ks.command;

import bg.tu_varna.ks.command.factories.CommandFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class responsible for recognizing and executing textual
 * commands entered by the user.
 * <p>
 * This class converts the input line from standard input into a command
 * name and a list of arguments, checks whether the command exists, and
 * then delegates the creation of the concrete
 * {@link bg.tu_varna.ks.contracts.Executable} to {@link CommandFactory}.
 * Arguments wrapped in double quotes are supported, so they can contain
 * whitespace.
 * </p>
 *
 * <h2>Example:</h2>
 * <pre>{@code
 * CommandRunner.run("book 2025-01-15 09:00 10:00 \"Team meeting\" \"discuss roadmap\"");
 * }</pre>
 *
 * @see Command
 * @see CommandFactory
 */
public class CommandRunner {

    /**
     * Processes a single line of user input.
     * <p>
     * The steps are: tokenizing the input, verifying that the command
     * exists, and delegating execution to the factory. If the command
     * name is unknown, a warning is printed and the user is directed
     * to the {@code help} command.
     * </p>
     *
     * @param input raw input string typed by the user; may be {@code null}
     *              or empty, in which case the method takes no action
     */
    public static void run(String input) {
        List<String> tokens = tokenize(input);

        if (tokens.isEmpty()) {
            return;
        }

        String commandName = tokens.get(0).toUpperCase();

        if (!check(commandName)) {
            System.out.println("Command doesn't exist! Type \"help\" for more information.");
            return;
        }

        List<String> arguments = tokens.subList(1, tokens.size());
        CommandFactory.getInstance().getExecutable(Command.valueOf(commandName), arguments).execute();
    }

    /**
     * Checks whether the given string matches the name of an existing
     * command in the {@link Command} enum.
     *
     * @param input the command name (case-insensitive)
     * @return {@code true} if such a command exists, otherwise {@code false}
     */
    public static boolean check(String input) {
        for (Command command : Command.values()) {
            if (command.getName().equalsIgnoreCase(input)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Splits the input string into individual tokens (the command name
     * and its arguments).
     * <p>
     * The following rules apply:
     * </p>
     * <ul>
     *   <li>Whitespace characters outside quotes separate tokens.</li>
     *   <li>Text between double quotes is treated as a single token,
     *       even if it contains whitespace.</li>
     *   <li>The quotes themselves are not included in the result.</li>
     * </ul>
     *
     * @param input the input string to split; may be {@code null}
     * @return a list of tokens; never {@code null}, but may be empty
     */
    private static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();

        if (input == null || input.isBlank()) {
            return tokens;
        }

        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < input.length(); i++) {
            char symbol = input.charAt(i);

            if (symbol == '"') {
                insideQuotes = !insideQuotes;
                continue;
            }

            if (Character.isWhitespace(symbol) && !insideQuotes) {
                if (!current.isEmpty()) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                continue;
            }

            current.append(symbol);
        }

        if (!current.isEmpty()) {
            tokens.add(current.toString());
        }

        return tokens;
    }
}
