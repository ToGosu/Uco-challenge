package co.edu.uco.ucochallenge.crosscuting.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para UcoChallengeException")
class UcoChallengeExceptionTest {

    @Test
    @DisplayName("Debería crear excepción con mensaje")
    void shouldCreateExceptionWithMessage() {
        // Arrange
        String message = "Error de prueba";

        // Act
        UcoChallengeException exception = new UcoChallengeException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Debería crear excepción con mensaje y causa")
    void shouldCreateExceptionWithMessageAndCause() {
        // Arrange
        String message = "Error de prueba";
        Throwable cause = new RuntimeException("Causa raíz");

        // Act
        UcoChallengeException exception = new UcoChallengeException(message, cause);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
