package pin.company.utils;

import java.text.SimpleDateFormat;     // класс для форматирования и парсинга дат
import java.util.Date;

public class DateUtils {
    // формат даты, который используется во всей программе
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    // объект форматтера для работы с датами
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    // чтобы даты проверялись строго по формату (например, 32-01-2020 будет ошибкой)
    static {
        dateFormat.setLenient(false);
    }

    // Метод форматирования даты в строку по заданному формату
    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }

}
