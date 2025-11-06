package co.edu.uco.ucochallenge.crosscuting.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class DateHelper {

    private DateHelper() {
    }

    public static LocalDate getDefaultDate() {
        return LocalDate.of(1900, 1, 1);
    }

    public static LocalDate getDefault(final LocalDate date) {
        return ObjectHelper.getDefault(date, getDefaultDate());
    }

    public static LocalDateTime getDefaultDateTime() {
        return LocalDateTime.of(1900, 1, 1, 0, 0, 0);
    }

    public static LocalDateTime getDefault(final LocalDateTime dateTime) {
        return ObjectHelper.getDefault(dateTime, getDefaultDateTime());
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static LocalDate today() {
        return LocalDate.now();
    }
}
