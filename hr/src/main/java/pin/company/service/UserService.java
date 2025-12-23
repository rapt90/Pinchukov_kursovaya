package pin.company.service;

import pin.company.model.AppState;
import pin.company.model.User;
import pin.company.model.UserRole;
import pin.company.ui.ConsoleHelper;

import java.util.Scanner;

public class UserService {
    private static final int MAX_LOGIN_LENGTH = 20;
    private static final int MAX_PASSWORD_LENGTH = 15;
    private static final int MAX_USERS = 50;

    private final ValidationService validationService;
    private final FileService fileService;

    public UserService(ValidationService validationService, FileService fileService) {
        this.validationService = validationService;
        this.fileService = fileService;
    }

    public boolean isLoginUnique(AppState state, String login) {
        return state.users.stream().noneMatch(user -> user.login.equals(login));
    }

    public boolean verifyCredentials(AppState state, String login, String password) {
        for (int i = 0; i < state.users.size(); i++) {
            User user = state.users.get(i);
            if (user.login.equals(login) && user.password.equals(password)) {
                state.currentUserId = i;
                state.currentUserRole = user.role;
                return true;
            }
        }
        return false;
    }

    public void addUser(AppState state, Scanner scanner) {
        if (state.users.size() >= MAX_USERS) {
            System.out.println("Достигнуто максимальное количество пользователей (" + MAX_USERS + ")");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        System.out.print("Введите логин (максимум " + MAX_LOGIN_LENGTH + " символов): ");
        String login = scanner.next();

        if (!isLoginUnique(state, login)) {
            System.out.println("Логин уже существует");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        if (!validationService.isLoginValid(login)) {
            System.out.println("Некорректный формат логина (максимум " + MAX_LOGIN_LENGTH + " символов)");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        System.out.print("Введите пароль (максимум " + MAX_PASSWORD_LENGTH + " символов): ");
        String password = scanner.next();

        if (!validationService.isPasswordValid(password)) {
            System.out.println("Некорректный формат пароля (максимум " + MAX_PASSWORD_LENGTH + " символов)");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        System.out.print("Введите роль (1 - admin, 2 - user): ");
        int roleValue = scanner.nextInt();

        if (roleValue != 1 && roleValue != 2) {
            System.out.println("Некорректная роль");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        UserRole role = UserRole.fromValue(roleValue);
        state.users.add(new User(login, password, role));

        if (fileService.saveUsers(state)) {
            System.out.println("Пользователь успешно добавлен");
        } else {
            System.out.println("Не удалось сохранить данные пользователя");
        }
        scanner.nextLine();
        ConsoleHelper.pressAnyKeyToContinue(scanner);
    }

    public void editUser(AppState state, Scanner scanner) {
        if (state.users.isEmpty()) {
            System.out.println("Пользователи не найдены");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        displayUsers(state);

        System.out.print("\nВыберите пользователя для редактирования (0 чтобы выйти): ");
        int choice = scanner.nextInt();

        if (choice < 1 || choice > state.users.size()) {
            if (choice != 0) {
                System.out.println("Некорректный выбор");
                ConsoleHelper.pressAnyKeyToContinue(scanner);
            }
            return;
        }

        int userIndex = choice - 1;
        if (userIndex == state.currentUserId) {
            System.out.println("Вы не можете редактировать свой собственный аккаунт");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        User user = state.users.get(userIndex);
        System.out.println("\nРедактирование пользователя: " + user.login);
        System.out.println("1. Изменение пароля");
        System.out.println("2. Изменение роли");
        System.out.println("0. Закончить редактирование");
        System.out.print("Выбор: ");
        choice = scanner.nextInt();

        switch (choice) {
            case 1:
                changeUserPassword(user, scanner);
                break;
            case 2:
                changeUserRole(user, scanner);
                break;
            case 0:
                return;
            default:
                System.out.println("Некорректный выбор");
                ConsoleHelper.pressAnyKeyToContinue(scanner);
                return;
        }

        if (fileService.saveUsers(state)) {
            System.out.println("Изменения успешно сохранены");
        } else {
            System.out.println("Не удалось сохранить изменения");
        }
        scanner.nextLine();
        ConsoleHelper.pressAnyKeyToContinue(scanner);
    }

    private void changeUserPassword(User user, Scanner scanner) {
        System.out.print("Новый пароль: ");
        String newPassword = scanner.next();

        if (!validationService.isPasswordValid(newPassword)) {
            System.out.println("Некорректный формат пароля (максимум " + MAX_PASSWORD_LENGTH + " символов)");
            return;
        }

        user.password = newPassword;
        System.out.println("Пароль успешно изменён");
    }

    private void changeUserRole(User user, Scanner scanner) {
        System.out.print("Новая роль (1 - admin, 2 - user): ");
        int roleValue = scanner.nextInt();

        if (roleValue == 1 || roleValue == 2) {
            user.role = UserRole.fromValue(roleValue);
            System.out.println("Роль успешно изменена");
        } else {
            System.out.println("Некорректная роль");
        }
    }

    public void deleteUser(AppState state, Scanner scanner) {
        if (state.users.isEmpty()) {
            System.out.println("Пользователи не найдены");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        displayUsers(state);

        System.out.print("\nВыберите пользователя для удаления (0 чтобы выйти): ");
        int choice = scanner.nextInt();

        if (choice < 1 || choice > state.users.size()) {
            if (choice != 0) {
                System.out.println("Некорректный выбор");
            }
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        int userIndex = choice - 1;
        if (userIndex == state.currentUserId) {
            System.out.println("Вы не можете удалить свой же аккаунт");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        User user = state.users.get(userIndex);
        System.out.print("Вы уверены, что хотите удалить пользователя " + user.login + "? (1 - Да, 0 - Нет): ");
        choice = scanner.nextInt();

        if (choice == 1) {
            state.users.remove(userIndex);

            // Корректируем currentUserId, если удалили пользователя с меньшим индексом
            if (state.currentUserId > userIndex) {
                state.currentUserId--;
            }

            if (fileService.saveUsers(state)) {
                System.out.println("Пользователь успешно удалён");
            } else {
                System.out.println("Не получилось сохранить изменения");
            }
        }
        scanner.nextLine();
        ConsoleHelper.pressAnyKeyToContinue(scanner);
    }

    public void displayUsers(AppState state) {
        if (state.users.isEmpty()) {
            System.out.println("Пользователи не найдены");
            return;
        }

        System.out.println("=== Список пользователей ===");
        System.out.println("Всего пользователей: " + state.users.size());

        for (int i = 0; i < state.users.size(); i++) {
            User user = state.users.get(i);
            String roleText = user.role == UserRole.ROLE_ADMIN ? "Администратор" : "Пользователь";

            System.out.printf("%d. Логин: %s | Роль: %s | Пароль: %s%n",
                    i + 1,
                    user.login,
                    roleText,
                    user.password);

            if (i == state.currentUserId) {
                System.out.println("   (текущий пользователь)");
            }
        }
    }

    public void displayUsersWithContinue(AppState state, Scanner scanner) {
        displayUsers(state);
        if (!state.users.isEmpty()) {
            ConsoleHelper.pressAnyKeyToContinue(scanner);
        }
    }


}