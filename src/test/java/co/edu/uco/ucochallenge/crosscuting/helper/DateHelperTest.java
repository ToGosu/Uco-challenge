package co.edu.uco.ucochallenge.crosscuting.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para DateHelper")
class DateHelperTest {

    @Test
    @DisplayName("Debería retornar fecha por defecto (1900-01-01)")
    void shouldReturnDefaultDate() {
        // Act
        LocalDate result = DateHelper.getDefaultDate();

        // Assert
        assertNotNull(result);
        assertEquals(LocalDate.of(1900, 1, 1), result);
    }

    @Test
    @DisplayName("Debería retornar la misma fecha si no es null")
    void shouldReturnSameDateWhenNotNull() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 12, 25);

        // Act
        LocalDate result = DateHelper.getDefault(date);

        // Assert
        assertEquals(date, result);
    }

    @Test
    @DisplayName("Debería retornar fecha por defecto si es null")
    void shouldReturnDefaultWhenDateIsNull() {
        // Arrange
        LocalDate date = null;

        // Act
        LocalDate result = DateHelper.getDefault(date);

        // Assert
        assertEquals(LocalDate.of(1900, 1, 1), result);
    }

    @Test
    @DisplayName("Debería retornar fecha-hora por defecto (1900-01-01 00:00:00)")
    void shouldReturnDefaultDateTime() {
        // Act
        LocalDateTime result = DateHelper.getDefaultDateTime();

        // Assert
        assertNotNull(result);
        assertEquals(LocalDateTime.of(1900, 1, 1, 0, 0, 0), result);
    }

    @Test
    @DisplayName("Debería retornar la misma fecha-hora si no es null")
    void shouldReturnSameDateTimeWhenNotNull() {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.of(2024, 12, 25, 15, 30, 45);

        // Act
        LocalDateTime result = DateHelper.getDefault(dateTime);

        // Assert
        assertEquals(dateTime, result);
    }

    @Test
    @DisplayName("Debería retornar fecha-hora por defecto si es null")
    void shouldReturnDefaultWhenDateTimeIsNull() {
        // Arrange
        LocalDateTime dateTime = null;

        // Act
        LocalDateTime result = DateHelper.getDefault(dateTime);

        // Assert
        assertEquals(LocalDateTime.of(1900, 1, 1, 0, 0, 0), result);
    }

    @Test
    @DisplayName("Debería retornar la fecha actual con now()")
    void shouldReturnCurrentDateTimeWithNow() {
        // Act
        LocalDateTime result = DateHelper.now();

        // Assert
        assertNotNull(result);
        assertTrue(result.isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(result.isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    @Test
    @DisplayName("Debería retornar la fecha de hoy con today()")
    void shouldReturnTodayWithToday() {
        // Act
        LocalDate result = DateHelper.today();

        // Assert
        assertNotNull(result);
        assertEquals(LocalDate.now(), result);
    }
}
