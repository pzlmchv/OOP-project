package bg.tu_varna.ks;

import bg.tu_varna.ks.command.CommandRunner;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        System.out.println("Calendar information system [Version 6.7]");
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
