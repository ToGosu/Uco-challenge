package co.edu.uco.ucochallenge.crosscuting.helper;

import co.edu.uco.ucochallenge.crosscuting.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para ValidationHelper")
class ValidationHelperTest {

    @Test
    @DisplayName("Debería validar email válido")
    void shouldAcceptValidEmail() {
        // Assert - No debe lanzar excepción
        assertDoesNotThrow(() -> ValidationHelper.validateEmail("test@example.com"));
        assertDoesNotThrow(() -> ValidationHelper.validateEmail("user.name+tag@example.co.uk"));
    }

    @Test
    @DisplayName("Debería rechazar email inválido")
    void shouldRejectInvalidEmail() {
        // Assert
        assertThrows(ValidationException.class, 
            () -> ValidationHelper.validateEmail("invalid-email"));
        
        assertThrows(ValidationException.class, 
            () -> ValidationHelper.validateEmail("@example.com"));
        
        assertThrows(ValidationException.class, 
            () -> ValidationHelper.validateEmail("test@"));
    }

    @Test
    @DisplayName("Debería rechazar email null o vacío")
    void shouldRejectNullOrEmptyEmail() {
        assertThrows(ValidationException.class, 
            () -> ValidationHelper.validateEmail(null));
        
        assertThrows(ValidationException.class, 
            () -> ValidationHelper.validateEmail(""));
        
        assertThrows(ValidationException.class, 
            () -> ValidationHelper.validateEmail("   "));
    }

    @Test
    @DisplayName("Debería validar número de celular válido")
    void shouldAcceptValidMobileNumber() {
        assertDoesNotThrow(() -> ValidationHelper.validateMobileNumber("3001234567"));
        assertDoesNotThrow(() -> ValidationHelper.validateMobileNumber("+573001234567"));
    }

    @Test
    @DisplayName("Debería rechazar número de celular inválido")
    void shouldRejectInvalidMobileNumber() {
        assertThrows(ValidationException.class, 
            () -> ValidationHelper.validateMobileNumber("123")); // Muy corto
        
        assertThrows(ValidationException.class, 
            () -> ValidationHelper.validateMobileNumber("abcd1234567")); // Letras
    }

    @Test
    @DisplayName("Debería validar campos requeridos")
    void shouldValidateRequiredFields() {
        assertDoesNotThrow(() -> ValidationHelper.validateRequired("Value", "Field"));
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> ValidationHelper.validateRequired("", "Field"));
        assertTrue(ex.getMessage().contains("Field es obligatorio"));
    }

    @Test
    @DisplayName("Debería validar longitud máxima")
    void shouldValidateMaxLength() {
        assertDoesNotThrow(() -> ValidationHelper.validateMaxLength("Hello", 10, "Field"));
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> ValidationHelper.validateMaxLength("Very long text", 5, "Field"));
        assertTrue(ex.getMessage().contains("no puede tener más de"));
    }
}