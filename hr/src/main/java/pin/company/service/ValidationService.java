package pin.company.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidationService {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final int MAX_LOGIN_LENGTH = 20;
    private static final int MAX_PASSWORD_LENGTH = 15;

    private final java.text.SimpleDateFormat dateFormat;

    // Конструктор: создаёт форматтер и запрещает "мягкий" парсинг (lenient = false)
    public ValidationService() {
        this.dateFormat = new java.text.SimpleDateFormat(DATE_FORMAT);
        this.dateFormat.setLenient(false);
    }

    // Проверка корректности даты (строка должна строго соответствовать формату dd-MM-yyyy)
    public boolean isValidDate(String date) {
        if (date == null) {
            return false; // null не является корректной датой
        }

        try {
            java.util.Date parsedDate = dateFormat.parse(date); // пробуем распарсить строку
            String reformatted = dateFormat.format(parsedDate); // форматируем обратно
            return reformatted.equals(date); // сравниваем с исходной строкой
        } catch (java.text.ParseException e) {
            return false; // если парсинг не удался — дата некорректна
        }
    }

    public boolean isLoginValid(String login) {
        return login != null && !login.isEmpty() && login.length() <= MAX_LOGIN_LENGTH;
    }

    public boolean isPasswordValid(String password) {
        return password != null && !password.isEmpty() && password.length() <= MAX_PASSWORD_LENGTH;
    }

    // Парсинг строки в объект Date. Если ошибка — возвращает null
    public java.util.Date parseDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (java.text.ParseException e) {
            return null;
        }
    }

}
