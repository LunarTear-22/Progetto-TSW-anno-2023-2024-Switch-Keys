package SwitchAndKeysUtil;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    // Converts LocalDate to Date
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // Converts Date to LocalDate
    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // Formats Date to "dd/MM/yyyy" string
    public static String formatDate(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = toLocalDate(date);
        return localDate.format(formatter);
    }

    // Formats LocalDate to "dd/MM/yyyy" string
    public static String formatLocalDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDate.format(formatter);
    }

    // Parses a Timestamp into LocalDate
    public static LocalDate parseToLocalDate(Timestamp timestamp) {
        // Converts Timestamp directly to LocalDate
        return timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // Formats Timestamp to "dd/MM/yyyy" string
    public static String formatTimestamp(Timestamp timestamp) {
        LocalDate localDate = parseToLocalDate(timestamp);
        return formatLocalDate(localDate);
    }
}
