package co.edu.uco.ucochallenge.crosscuting.exception.user;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para UserAlreadyExistsException")
class UserAlreadyExistsExceptionTest {

    @Test
    @DisplayName("Debería heredar de UcoChallengeException")
    void shouldInheritFromUcoChallengeException() {
        // Act
        UserAlreadyExistsException exception = new UserAlreadyExistsException("Test");

        // Assert
        assertTrue(exception instanceof UcoChallengeException);
    }

    @Test
    @DisplayName("Debería crear excepción con mensaje descriptivo")
    void shouldCreateExceptionWithDescriptiveMessage() {
        // Arrange
        String message = "El usuario ya existe con el email: test@example.com";

        // Act
        UserAlreadyExistsException exception = new UserAlreadyExistsException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNotNull(exception);
    }
}
