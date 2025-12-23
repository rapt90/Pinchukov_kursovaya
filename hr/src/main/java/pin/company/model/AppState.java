package pin.company.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс AppState — хранит текущее состояние приложения.
 * Используется как центральное хранилище данных:
 * - список сотрудников
 * - список пользователей
 * - информация о текущем вошедшем пользователе
 */
public class AppState implements Serializable {
    @java.io.Serial
    private static final long serialVersionUID = 1L; // идентификатор версии для сериализации

    public List<Employee> employees = new ArrayList<>(); // список хранит объекты только типа employee

    public List<User> users = new ArrayList<>();

    public int currentUserId = -1;  //Индекс текущего пользователя в списке users. -1 означает, что пользователь ещё не авторизован.

    public UserRole currentUserRole = UserRole.ROLE_USER;
}
