package pin.company.ui;

import pin.company.model.AppState;
import pin.company.service.*;

import java.util.Scanner;

public class UserMenu {
    private final AppState state;
    private final Scanner scanner;
    private final EmployeeService employeeService;

    public UserMenu(AppState state, Scanner scanner, FileService fileService) {
        this.state = state;
        this.scanner = scanner;
        ValidationService validationService = new ValidationService();
        this.employeeService = new EmployeeService(validationService, fileService);
    }

    public void showUserMenu() {
        int choice;

        do {
            System.out.println("\n=== МЕНЮ ПОЛЬЗОВАТЕЛЯ ===");
            System.out.println("1. Показать список сотрудников");
            System.out.println("2. Сортировать сотрудников по параметрам");
            System.out.println("3. Поиск сотрудников по параметрам");
            System.out.println("4. Вывести список пенсионеров");
            System.out.println("5. Вывести сотрудников в порядке убывания стажа");
            System.out.println("0. Выйти из аккаунта");
            System.out.print("\nВыбор: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Очистка буфера
            } catch (Exception e) {
                System.out.println("Ошибка: введите число");
                scanner.nextLine();
                choice = -1;
            }

            switch (choice) {
                case 1:
                    employeeService.displayEmployees(state, scanner);
                    break;
                case 2:
                    employeeService.sortEmployees(state, scanner);
                    break;
                case 3:
                    employeeService.searchEmployees(state, scanner);
                    break;
                case 4:
                    employeeService.displayPensioners(state, scanner);
                    break;
                case 5:
                    employeeService.displayEmployeeExperience(state, scanner);
                    break;
                case 0:
                    state.currentUserId = -1;
                    System.out.println("Выход из аккаунта...");
                    break;
                default:
                    System.out.println("Некорректный выбор");
                    ConsoleHelper.pressAnyKeyToContinue(scanner);
                    break;
            }
        } while (choice != 0);
    }
}