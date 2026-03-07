package bg.tu_varna.ks.command;

import bg.tu_varna.ks.command.factories.CommandFactory;

import java.util.Arrays;
import java.util.List;

public class CommandRunner {
    public static void run(String command) {
        List<String> arguments = Arrays.stream(command.split(" ")).toList();
        command = parse(command);

        if (command == null) {
            return;
        }

        if (!check(command)) {
            System.out.println("Command doesn't exist! Type \"help\" for more information.");
            return;
        }
        arguments = arguments.subList(1, arguments.size());
        CommandFactory.getInstance().getExecutable(Command.valueOf(command), arguments).execute();
    }

    public static String parse(String input) {
        if (input.isBlank()) {
            return null;
        }

        return input.split(" ")[0].toUpperCase();
    }

    public static boolean check(String input) {
        for (Command command : Command.values()) {
            if (command.getName().equalsIgnoreCase(input)) {
                return true;
            }
        }

        return false;
    }
}
