package pin.company.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    static {
        dateFormat.setLenient(false);
    }

    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    public static Date parseDate(String dateStr) throws ParseException {
        return dateFormat.parse(dateStr);
    }

    public static boolean isValidDate(String date) {
        if (date == null) {
            return false;
        }

        try {
            Date parsedDate = dateFormat.parse(date);
            String reformatted = dateFormat.format(parsedDate);
            return reformatted.equals(date);
        } catch (ParseException e) {
            return false;
        }
    }

    public static long calculateDifferenceInDays(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    public static String formatExperience(long days) {
        long years = days / 365;
        long remainingDays = days % 365;
        return String.format("%d лет, %d дней", years, remainingDays);
    }
}