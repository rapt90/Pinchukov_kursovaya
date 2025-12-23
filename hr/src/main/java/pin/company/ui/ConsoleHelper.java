package pin.company.ui; // пакет, где хранится вспомогательный класс для работы с консолью

import java.util.Scanner; // класс для ввода данных с консоли

public class ConsoleHelper {

    /**
     * Очищает буфер ввода.
     * Используется после вызова scanner.nextInt() или scanner.next(),
     * чтобы убрать оставшийся символ новой строки '\n'.
     */
    public static void clearInputBuffer(Scanner scanner) {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }

    /**
     * Делает паузу в программе, пока пользователь не нажмёт Enter.
     * Удобно использовать после вывода информации, чтобы пользователь успел её прочитать.
     */
    public static void pressAnyKeyToContinue(Scanner scanner) {
        System.out.println("\nНажмите Enter чтобы продолжить...");
        scanner.nextLine(); // ждём, пока пользователь нажмёт Enter
    }

    public static String getLimitedInput(Scanner scanner, String prompt, int maxLength) {
        String input;
        do {
            System.out.print(prompt + " (макс. " + maxLength + " символов): "); // выводим подсказку
            input = scanner.nextLine(); // читаем строку

            // проверяем длину
            if (input.length() > maxLength) {
                System.out.println("Ошибка: введено " + input.length() +
                        " символов. Допустимо не более " + maxLength + ".");
                System.out.println("Попробуйте снова.\n");
            } else if (input.isEmpty()) {
                System.out.println("Ошибка: значение не может быть пустым. Попробуйте снова.\n");
            }

        } while (input.isEmpty() || input.length() > maxLength); // повторяем, пока ввод некорректный

        return input; // возвращаем валидное значение
    }

}
