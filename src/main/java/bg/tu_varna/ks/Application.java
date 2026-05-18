package bg.tu_varna.ks;

import bg.tu_varna.ks.command.CommandRunner;

import java.util.Scanner;

/**
 * Main entry point of the calendar information system.
 * <p>
 * The {@code Application} class starts the program, prints the welcome
 * banner and initializes an infinite REPL (Read-Eval-Print Loop) in which
 * the user types commands on standard input. Every line of input is
 * forwarded to {@link bg.tu_varna.ks.command.CommandRunner} for parsing
 * and execution.
 * </p>
 *
 * <p>The application can only be terminated through the
 * {@link bg.tu_varna.ks.command.utility.Exit} command, which calls
 * {@link System#exit(int)}.</p>
 *
 * <h2>How to run:</h2>
 * <pre>{@code
 * java -cp out\artifacts\calendar.jar bg.tu_varna.ks.Application
 * }</pre>
 *
 * @author pzlmchv 24621854
 * @version 9.11
 */
public class Application {

    /**
     * Program entry point.
     * <p>
     * Prints version information and then, in an infinite loop, reads one
     * line at a time from {@code System.in} and passes it to
     * {@link CommandRunner#run(String)} for processing.
     * </p>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Calendar information system [Version 9.11]");
        System.out.println("(c) pzlmchv 24621854. All rights reserved.");
        System.out.println();
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.print(">");
            String input = scanner.nextLine();
            System.out.println();
            CommandRunner.run(input);
            System.out.print("\n");
        } while(true);
    }
}
