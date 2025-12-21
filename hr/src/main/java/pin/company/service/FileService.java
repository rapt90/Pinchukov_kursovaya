package pin.company.service;

import pin.company.model.AppState;
import pin.company.model.User;
import pin.company.model.UserRole;

import java.io.*;
import java.util.List;

public class FileService {
    private static final String EMPLOYEES_FILE = "employees.dat";
    private static final String USERS_FILE = "users.dat";

    @SuppressWarnings("unchecked")
    public boolean loadEmployees(AppState state) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EMPLOYEES_FILE))) {
            state.employees = (List<pin.company.model.Employee>) ois.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public boolean loadUsers(AppState state) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            state.users = (List<User>) ois.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    public boolean saveEmployees(AppState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EMPLOYEES_FILE))) {
            oos.writeObject(state.employees);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean saveUsers(AppState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(state.users);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void createDefaultAdmin(AppState state) {
        state.users.clear();
        state.users.add(new User("admin", "admin", UserRole.ROLE_ADMIN));

        if (!saveUsers(state)) {
            System.out.println("Error creating users file");
        } else {
            System.out.println("Default admin created. Login: admin Password: admin");
        }
    }

    public void createEmptyEmployeesFile(AppState state) {
        state.employees.clear();

        if (!saveEmployees(state)) {
            System.out.println("Error creating orders file");
        } else {
            System.out.println("Empty orders file created");
        }
    }
}