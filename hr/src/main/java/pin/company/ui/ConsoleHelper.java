package pin.company.ui;

import java.util.Scanner;

public class ConsoleHelper {
    public static void clearInputBuffer(Scanner scanner) {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }

    public static void pressAnyKeyToContinue(Scanner scanner) {
        System.out.println("\nНажмите Enter чтобы продолжить...");
            scanner.nextLine();

    }

    public static String getLimitedInput(Scanner scanner, String prompt, int maxLength) {
        System.out.print(prompt + " (макс. " + maxLength + " символов): ");
        String input = scanner.nextLine();
        if (input.length() > maxLength) {
            System.out.println("Внимание: введено " + input.length() + " символов, будет обрезано до " + maxLength);
            return input.substring(0, maxLength);
        }
        return input;
    }
}