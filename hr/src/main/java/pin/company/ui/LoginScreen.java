package pin.company.ui;

import pin.company.model.AppState;
import pin.company.service.ValidationService;

import java.util.Scanner; // класс для ввода данных с консоли

public class LoginScreen {

    private static final int MAX_ATTEMPTS = 3;

    private final AppState state;
    private final Scanner scanner;
    private final ValidationService validationService;


    public LoginScreen(AppState state, Scanner scanner) {
        this.state = state;
        this.scanner = scanner;
        this.validationService = new ValidationService(); // создаём сервис валидации
    }


    public void showLoginScreen() {
        int attempts = 0; // счётчик попыток входа

        // пока пользователь не авторизован (currentUserId == -1)
        while (state.currentUserId == -1) {
            System.out.println("\n=== Система управления отдела кадров предприятия ===");
            System.out.println("Пожалуйста войдите в аккаунт чтобы продолжить\n");

            System.out.print("Login: ");
            String login = scanner.next();

            System.out.print("Password: ");
            String password = scanner.next();
            scanner.nextLine(); // очистка буфера после ввода

            // проверка формата логина
            if (!validationService.isLoginValid(login)) {
                System.out.println("Неправильный формат логина (максимум " + 20 + " символов)");
                ConsoleHelper.pressAnyKeyToContinue(scanner);
                attempts++;
            }
            // проверка формата пароля
            else if (!validationService.isPasswordValid(password)) {
                System.out.println("Неправильный формат пароля (максимум " + 15 + " символов)");
                ConsoleHelper.pressAnyKeyToContinue(scanner);
                attempts++;
            }
            // проверка правильности логина и пароля
            else if (validationService.verifyCredentials(state, login, password)) {
                System.out.println("\nЛогин и пароль верны! Добро пожаловать, " + login + ".");
                ConsoleHelper.pressAnyKeyToContinue(scanner);
                return; // успешный вход — выходим из метода
            }
            else {
                System.out.println("\nНеправильный логин или пароль");
                ConsoleHelper.pressAnyKeyToContinue(scanner);
                attempts++;
            }

            // проверка количества попыток
            if (attempts >= MAX_ATTEMPTS) {
                System.out.println("\nВы исчерпали количество попыток входа (" + MAX_ATTEMPTS + "). Программа завершает работу.");
                System.exit(0); // завершение программы
            } else {
                int remaining = MAX_ATTEMPTS - attempts; // оставшиеся попытки
                System.out.println("Осталось попыток: " + remaining);
            }
        }
    }


}
