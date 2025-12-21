package pin.company.ui;

import pin.company.model.AppState;
import pin.company.service.ValidationService;

import java.util.Scanner;

public class LoginScreen {
    private static final int MAX_ATTEMPTS = 3;

    private final AppState state;
    private final Scanner scanner;
    private final ValidationService validationService;

    public LoginScreen(AppState state, Scanner scanner) {
        this.state = state;
        this.scanner = scanner;
        this.validationService = new ValidationService();
    }

    public void showLoginScreen() {
        int attempts = 0;

        while (state.currentUserId == -1) {
            System.out.println("\n=== Система управления отдела кадров предприятия ===");
            System.out.println("Пожалуйста войдите в аккаунт чтобы продолжить\n");

            System.out.print("Login: ");
            String login = scanner.next();
            System.out.print("Password: ");
            String password = scanner.next();
            scanner.nextLine();

            if (!validationService.isLoginValid(login)) {
                System.out.println("Неправильный формат логина (максимум " + 20 + " символов)");
                ConsoleHelper.pressAnyKeyToContinue(scanner);
                attempts++;
            } else if (!validationService.isPasswordValid(password)) {
                System.out.println("Неправильный формат пароля (максимум " + 15 + " символов)");
                ConsoleHelper.pressAnyKeyToContinue(scanner);
                attempts++;
            } else if (verifyCredentials(login, password)) {
                System.out.println("\nЛогин и пароль верны! Добро пожаловать, " + login + ".");
                ConsoleHelper.pressAnyKeyToContinue(scanner);
                return;
            } else {
                System.out.println("\nНеправильный логин или пароль");
                ConsoleHelper.pressAnyKeyToContinue(scanner);
                attempts++;
            }

            if (attempts >= MAX_ATTEMPTS) {
                System.out.println("\nВы исчерпали количество попыток входа (" + MAX_ATTEMPTS + "). Программа завершает работу.");
                System.exit(0);
            } else {
                int remaining = MAX_ATTEMPTS - attempts;
                System.out.println("Осталось попыток: " + remaining);
            }
        }
    }

    private boolean verifyCredentials(String login, String password) {
        for (int i = 0; i < state.users.size(); i++) {
            pin.company.model.User user = state.users.get(i);
            if (user.login.equals(login) && user.password.equals(password)) {
                state.currentUserId = i;
                state.currentUserRole = user.role;
                return true;
            }
        }
        return false;
    }
}