package bg.tu_varna.ks.command;

import bg.tu_varna.ks.command.factories.CommandFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandRunner {
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

    public static boolean check(String input) {
        for (Command command : Command.values()) {
            if (command.getName().equalsIgnoreCase(input)) {
                return true;
            }
        }

        return false;
    }

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
