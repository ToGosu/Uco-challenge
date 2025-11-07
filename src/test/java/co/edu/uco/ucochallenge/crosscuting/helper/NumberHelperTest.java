package co.edu.uco.ucochallenge.crosscuting.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para NumberHelper")
class NumberHelperTest {

    @Test
    @DisplayName("Debería retornar cero como default int")
    void shouldReturnZeroAsDefaultInt() {
        // Act
        int result = NumberHelper.getDefaultInt();

        // Assert
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Debería retornar el mismo valor si Integer no es null")
    void shouldReturnSameValueWhenIntegerNotNull() {
        // Arrange
        Integer value = 42;

        // Act
        int result = NumberHelper.getDefault(value);

        // Assert
        assertEquals(42, result);
    }

    @Test
    @DisplayName("Debería retornar cero si Integer es null")
    void shouldReturnZeroWhenIntegerIsNull() {
        // Arrange
        Integer value = null;

        // Act
        int result = NumberHelper.getDefault(value);

        // Assert
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Debería retornar cero como default long")
    void shouldReturnZeroAsDefaultLong() {
        // Act
        long result = NumberHelper.getDefaultLong();

        // Assert
        assertEquals(0L, result);
    }

    @Test
    @DisplayName("Debería retornar el mismo valor si Long no es null")
    void shouldReturnSameValueWhenLongNotNull() {
        // Arrange
        Long value = 100L;

        // Act
        long result = NumberHelper.getDefault(value);

        // Assert
        assertEquals(100L, result);
    }

    @Test
    @DisplayName("Debería retornar cero si Long es null")
    void shouldReturnZeroWhenLongIsNull() {
        // Arrange
        Long value = null;

        // Act
        long result = NumberHelper.getDefault(value);

        // Assert
        assertEquals(0L, result);
    }

    @Test
    @DisplayName("Debería retornar cero como default double")
    void shouldReturnZeroAsDefaultDouble() {
        // Act
        double result = NumberHelper.getDefaultDouble();

        // Assert
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Debería retornar el mismo valor si Double no es null")
    void shouldReturnSameValueWhenDoubleNotNull() {
        // Arrange
        Double value = 3.14;

        // Act
        double result = NumberHelper.getDefault(value);

        // Assert
        assertEquals(3.14, result);
    }

    @Test
    @DisplayName("Debería retornar cero si Double es null")
    void shouldReturnZeroWhenDoubleIsNull() {
        // Arrange
        Double value = null;

        // Act
        double result = NumberHelper.getDefault(value);

        // Assert
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Debería detectar números positivos")
    void shouldDetectPositiveNumbers() {
        // Assert
        assertTrue(NumberHelper.isPositive(1));
        assertTrue(NumberHelper.isPositive(100));
        assertFalse(NumberHelper.isPositive(0));
        assertFalse(NumberHelper.isPositive(-1));
    }

    @Test
    @DisplayName("Debería detectar números negativos")
    void shouldDetectNegativeNumbers() {
        // Assert
        assertTrue(NumberHelper.isNegative(-1));
        assertTrue(NumberHelper.isNegative(-100));
        assertFalse(NumberHelper.isNegative(0));
        assertFalse(NumberHelper.isNegative(1));
    }

    @Test
    @DisplayName("Debería detectar cero")
    void shouldDetectZero() {
        // Assert
        assertTrue(NumberHelper.isZero(0));
        assertFalse(NumberHelper.isZero(1));
        assertFalse(NumberHelper.isZero(-1));
    }
}

