package pin.company.main;

import pin.company.model.AppState;
import pin.company.service.FileService;
import pin.company.ui.LoginScreen;
import pin.company.ui.UserMenu;
import pin.company.ui.AdminMenu;

import java.util.Scanner;

public class Main {
    private final AppState state;
    private final Scanner scanner;
    private final FileService fileService;
    private final LoginScreen loginScreen;
    private final UserMenu userMenu;
    private final AdminMenu adminMenu;

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
            fileService.createDefaultAdmin(state);
            usersLoaded = fileService.loadUsers(state);
        }

        if (!employeesLoaded) {
            fileService.createEmptyEmployeesFile(state);
            employeesLoaded = fileService.loadEmployees(state);
        }

        return usersLoaded && employeesLoaded;
    }

    public void run() {
        if (!initializeData()) {
            System.out.println("Не удалось инициализировать данные");
            return;
        }

        while (true) {
            loginScreen.showLoginScreen();

            if (state.currentUserId == -1) {
                break;
            }

            if (state.currentUserRole == pin.company.model.UserRole.ROLE_ADMIN) {
                adminMenu.showAdminMenu();
            } else {
                userMenu.showUserMenu();
            }
        }

        System.out.println("\nДо свидания!");
        scanner.close();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}