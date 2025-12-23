package pin.company.service; // пакет, где хранится сервис для работы с файлами

import pin.company.model.AppState;
import pin.company.model.User;     // модель пользователя
import pin.company.model.UserRole;

import java.io.*;
import java.util.List;

public class FileService {
    private static final String EMPLOYEES_FILE = "employees.dat";
    private static final String USERS_FILE = "users.dat";

    @SuppressWarnings("unchecked") // подавляем предупреждение о приведении типов
    public boolean loadEmployees(AppState state) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EMPLOYEES_FILE))) {
            state.employees = (List<pin.company.model.Employee>) ois.readObject(); // десериализация списка сотрудников
            return true; // успешная загрузка
        } catch (IOException | ClassNotFoundException e) {
            return false; // ошибка при чтении файла
        }
    }

    @SuppressWarnings("unchecked")
    public boolean loadUsers(AppState state) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            state.users = (List<User>) ois.readObject(); // десериализация списка пользователей
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    public boolean saveEmployees(AppState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EMPLOYEES_FILE))) {
            oos.writeObject(state.employees); // сериализация списка сотрудников
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean saveUsers(AppState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(state.users); // сериализация списка пользователей
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void createDefaultAdmin(AppState state) {
        // очищаем список пользователей
        state.users.clear();
        // добавляем дефолтного администратора с логином/паролем "admin"
        state.users.add(new User("admin", "admin", UserRole.ROLE_ADMIN));

        if (!saveUsers(state)) {
            System.out.println("Error creating users file");
        } else {
            System.out.println("Default admin created. Login: admin Password: admin"); // сообщение об успешном создании
        }
    }

    public void createEmptyEmployeesFile(AppState state) {
        // очищаем список сотрудников
        state.employees.clear();

        // сохраняем пустой список сотрудников в файл
        if (!saveEmployees(state)) {
            System.out.println("Error creating orders file"); // ошибка при создании файла
        } else {
            System.out.println("Empty orders file created"); // сообщение об успешном создании
        }
    }
}
