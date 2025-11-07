package co.edu.uco.ucochallenge.crosscuting.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para ValidationException")
class ValidationExceptionTest {

    @Test
    @DisplayName("Debería heredar de UcoChallengeException")
    void shouldInheritFromUcoChallengeException() {
        // Act
        ValidationException exception = new ValidationException("Test");

        // Assert
        assertTrue(exception instanceof UcoChallengeException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Debería crear excepción con mensaje")
    void shouldCreateExceptionWithMessage() {
        // Arrange
        String message = "Validación fallida";

        // Act
        ValidationException exception = new ValidationException(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("Debería crear excepción con mensaje y causa")
    void shouldCreateExceptionWithMessageAndCause() {
        // Arrange
        String message = "Validación fallida";
        Throwable cause = new IllegalArgumentException("Argumento inválido");

        // Act
        ValidationException exception = new ValidationException(message, cause);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
