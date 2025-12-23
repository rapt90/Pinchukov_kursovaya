package pin.company.utils;

import java.text.ParseException;       // исключение при ошибке парсинга даты
import java.text.SimpleDateFormat;     // класс для форматирования и парсинга дат
import java.util.Date;

public class DateUtils {
    // формат даты, который используется во всей программе
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    // объект форматтера для работы с датами
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    // статический блок: запрещаем "мягкий" парсинг (lenient = false),
    // чтобы даты проверялись строго по формату (например, 32-01-2020 будет ошибкой)
    static {
        dateFormat.setLenient(false);
    }

    // Метод форматирования даты в строку по заданному формату
    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    // Метод парсинга строки в объект Date
    // выбрасывает ParseException, если строка не соответствует формату
    public static Date parseDate(String dateStr) throws ParseException {
        return dateFormat.parse(dateStr);
    }

    // Проверка корректности строки даты
    public static boolean isValidDate(String date) {
        if (date == null) {
            return false; // null не является корректной датой
        }

        try {
            // пробуем распарсить строку
            Date parsedDate = dateFormat.parse(date);
            // форматируем обратно в строку
            String reformatted = dateFormat.format(parsedDate);
            // сравниваем с исходной строкой — если совпадает, значит формат корректный
            return reformatted.equals(date);
        } catch (ParseException e) {
            return false; // если парсинг не удался — дата некорректна
        }
    }

    // Метод для форматирования стажа (опыта работы) в годах и днях
    public static String formatExperience(long days) {
        long years = days / 365;
        long remainingDays = days % 365;     // остаток дней
        return String.format("%d лет, %d дней", years, remainingDays);
    }
}
