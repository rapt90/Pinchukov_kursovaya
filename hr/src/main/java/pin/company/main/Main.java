package pin.company.main;

import pin.company.model.AppState;
import pin.company.service.FileService;
import pin.company.ui.LoginScreen;
import pin.company.ui.UserMenu;
import pin.company.ui.AdminMenu;

import java.util.Scanner;

public class Main {
    private final AppState state;          // Хранит текущее состояние приложения (пользователи, сотрудники, текущая роль)
    private final Scanner scanner;         // Сканер для ввода данных с консоли
    private final FileService fileService; // Сервис для работы с файлами (загрузка/сохранение данных)
    private final LoginScreen loginScreen; // Экран авторизации
    private final UserMenu userMenu;       // Меню для обычного пользователя
    private final AdminMenu adminMenu;     // Меню для администратора

    /**
     * Конструктор приложения.
     * Инициализирует состояние, сканер и все экраны/меню.
     */
    public Main() {
        this.state = new AppState();
        this.scanner = new Scanner(System.in);
        this.fileService = new FileService();
        this.loginScreen = new LoginScreen(state, scanner);
        this.userMenu = new UserMenu(state, scanner, fileService);
        this.adminMenu = new AdminMenu(state, scanner, fileService);
    }

    private boolean initializeData() {
        state.currentUserId = -1;
        state.currentUserRole = pin.company.model.UserRole.ROLE_USER;

        boolean usersLoaded = fileService.loadUsers(state);
        boolean employeesLoaded = fileService.loadEmployees(state);

        if (!usersLoaded) {
            fileService.createDefaultAdmin(state); // создаём дефолтного админа
            usersLoaded = fileService.loadUsers(state);
        }

        if (!employeesLoaded) {
            fileService.createEmptyEmployeesFile(state); // создаём пустой файл сотрудников
            employeesLoaded = fileService.loadEmployees(state);
        }

        return usersLoaded && employeesLoaded;
    }


    public void run() { // основной цикл программы
        if (!initializeData()) {
            System.out.println("Не удалось инициализировать данные");
            return;
        }

        while (true) {
            loginScreen.showLoginScreen(); // экран авторизации

            if (state.currentUserId == -1) { // если пользователь не вошёл
                break;
            }

            if (state.currentUserRole == pin.company.model.UserRole.ROLE_ADMIN) {
                adminMenu.showAdminMenu(); // меню администратора
            } else {
                userMenu.showUserMenu();   // меню пользователя
            }
        }

        System.out.println("\nДо свидания!");
        scanner.close();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
