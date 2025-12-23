package pin.company.service;

import pin.company.model.AppState;
import pin.company.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidationService {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final int MAX_LOGIN_LENGTH = 20;
    private static final int MAX_PASSWORD_LENGTH = 15;

    private final SimpleDateFormat dateFormat;

    // Конструктор: создаёт форматтер и запрещает "мягкий" парсинг (lenient = false)
    public ValidationService() {
        this.dateFormat = new SimpleDateFormat(DATE_FORMAT);
        this.dateFormat.setLenient(false);
    }

    // Проверка корректности даты (строка должна строго соответствовать формату dd-MM-yyyy)
    public boolean isValidDate(String date) {
        if (date == null) {
            return false;
        }
        try {
            Date parsedDate = dateFormat.parse(date);
            String reformatted = dateFormat.format(parsedDate);
            return reformatted.equals(date); // сравнивает ссылки: возвращает true, если два объекта указывают на один и тот же участок памяти.
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean isLoginValid(String login) {
        return login != null && !login.isEmpty() && login.length() <= MAX_LOGIN_LENGTH;
    }

    public boolean isPasswordValid(String password) {
        return password != null && !password.isEmpty() && password.length() <= MAX_PASSWORD_LENGTH;
    }

    // Парсинг строки в объект Date. Если ошибка — возвращает null
    public Date parseDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    //  Проверка логина и пароля
    public boolean verifyCredentials(AppState state, String login, String password) {
        for (int i = 0; i < state.users.size(); i++) {
            User user = state.users.get(i);
            if (user.login.equals(login) && user.password.equals(password)) {
                state.currentUserId = i;       // сохраняем ID текущего пользователя
                state.currentUserRole = user.role; // сохраняем его роль
                return true; // успешная авторизация
            }
        }
        return false; // если совпадений нет
    }
}
