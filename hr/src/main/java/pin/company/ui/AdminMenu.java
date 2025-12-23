package pin.company.ui;

import pin.company.model.AppState;
import pin.company.model.Employee;
import pin.company.model.User;
import pin.company.model.UserRole;
import pin.company.service.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final AppState state;
    private final Scanner scanner;
    private final EmployeeService employeeService;
    private final UserService userService;

    public AdminMenu(AppState state, Scanner scanner, FileService fileService) {
        this.state = state; // сохраняем ссылку на текущее состояние
        this.scanner = scanner; // сохраняем сканнер для дальнейшего ввода
        ValidationService validationService = new ValidationService(); // создаём сервис валидации (проверка корректности данных, дат и т.п.)
        this.employeeService = new EmployeeService(validationService, fileService);
        this.userService = new UserService(validationService, fileService);
    }

    public void showAdminMenu() {
        int choice;

        do {
            System.out.println("\n=== МЕНЮ АДМИНИСТРАТОРА ===");
            System.out.println("1. Управление сотрудниками");
            System.out.println("2. Управление пользователями");
            System.out.println("0. Выйти из аккаунта");
            System.out.print("\nВыбор: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    manageEmployees();
                    break;
                case 2:
                    manageUsers();
                    break;
                case 0:
                    state.currentUserId = -1;
                    break;
                default:
                    System.out.println("Некорректный выбор");
                    ConsoleHelper.pressAnyKeyToContinue(scanner);
                    break;
            }
        } while (choice != 0);
    }

    private void manageEmployees() {
        int employeeChoice;
        do {
            System.out.println("\n=== Управление сотрудниками ===");
            System.out.println("1. Показать список сотрудников");
            System.out.println("2. Добавить сотрудника");
            System.out.println("3. Редактировать сотрудника");
            System.out.println("4. Удалить сотрудника");
            System.out.println("5. Сортировать сотрудников по параметрам");
            System.out.println("6. Поиск сотрудников по параметрам");
            System.out.println("7. Вывести список пенсионеров");
            System.out.println("8. Вывести сотрудников в порядке убывания стажа");
            System.out.println("0. Вернуться в главное меню");
            System.out.print("\nChoice: ");
            employeeChoice = scanner.nextInt();

            switch (employeeChoice) {
                case 1:
                    employeeService.displayEmployees(state, scanner);
                    break;
                case 2:
                    employeeService.addEmployee(state, scanner);
                    break;
                case 3:
                    employeeService.editEmployee(state, scanner);
                    break;
                case 4:
                    employeeService.deleteEmployee(state, scanner);
                    break;
                case 5:
                    employeeService.sortEmployees(state, scanner);
                    break;
                case 6:
                    employeeService.searchEmployees(state, scanner);
                    break;
                case 7:
                    employeeService.displayPensioners(state, scanner);
                    break;
                case 8:
                    employeeService.displayEmployeeExperience(state, scanner);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Некорректный выбор");
                    ConsoleHelper.pressAnyKeyToContinue(scanner);
                    break;
            }
        } while (employeeChoice != 0);
    }

    private void manageUsers() {
        int userChoice;
        do {
            System.out.println("\n=== Управление пользователями ===");
            System.out.println("1. Вывести список пользователей");
            System.out.println("2. Добавить пользователя");
            System.out.println("3. Редактировать пользователя");
            System.out.println("4. Удалить пользователя");
            System.out.println("0. Вернуться в главное меню");
            System.out.print("\nВыбор: ");
            userChoice = scanner.nextInt();

            switch (userChoice) {
                case 1:
                    userService.displayUsersWithContinue(state, scanner);
                    break;
                case 2:
                    userService.addUser(state, scanner);
                    break;
                case 3:
                    userService.editUser(state, scanner);
                    break;
                case 4:
                    userService.deleteUser(state, scanner);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Некорректный выбор");
                    ConsoleHelper.pressAnyKeyToContinue(scanner);
                    break;
            }
        } while (userChoice != 0);
    }

}