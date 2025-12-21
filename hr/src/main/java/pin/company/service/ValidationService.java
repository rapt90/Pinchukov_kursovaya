package pin.company.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidationService {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final int MAX_LOGIN_LENGTH = 20;
    private static final int MAX_PASSWORD_LENGTH = 15;

    private final java.text.SimpleDateFormat dateFormat;

    public ValidationService() {
        this.dateFormat = new java.text.SimpleDateFormat(DATE_FORMAT);
        this.dateFormat.setLenient(false);
    }

    public boolean isValidDate(String date) {
        if (date == null) {
            return false;
        }

        try {
            java.util.Date parsedDate = dateFormat.parse(date);
            String reformatted = dateFormat.format(parsedDate);
            return reformatted.equals(date);
        } catch (java.text.ParseException e) {
            return false;
        }
    }

    public boolean isLoginValid(String login) {
        return login != null && !login.isEmpty() && login.length() <= MAX_LOGIN_LENGTH;
    }

    public boolean isPasswordValid(String password) {
        return password != null && !password.isEmpty() && password.length() <= MAX_PASSWORD_LENGTH;
    }

    public java.util.Date parseDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (java.text.ParseException e) {
            return null;
        }
    }

    // Новый метод для проверки сложности пароля (опционально)
    public boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }

        return hasLetter && hasDigit;
    }

    // Метод для проверки формата логина (только буквы и цифры)
    public boolean isLoginFormatValid(String login) {
        if (login == null || login.isEmpty()) {
            return false;
        }

        for (char c : login.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }

        return true;
    }
}