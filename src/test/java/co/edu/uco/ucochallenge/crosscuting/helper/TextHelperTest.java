package co.edu.uco.ucochallenge.crosscuting.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para TextHelper")
class TextHelperTest {

    @Test
    @DisplayName("Debería retornar string vacío por defecto")
    void shouldReturnEmptyStringByDefault() {
        // Arrange (Preparar)
        // No hay preparación necesaria
        
        // Act (Actuar)
        String result = TextHelper.getDefault();
        
        // Assert (Verificar)
        assertEquals("", result);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Debería retornar el mismo valor si no es null")
    void shouldReturnSameValueWhenNotNull() {
        // Arrange
        String value = "Hello World";
        
        // Act
        String result = TextHelper.getDefault(value);
        
        // Assert
        assertEquals("Hello World", result);
    }

    @Test
    @DisplayName("Debería retornar string vacío si el valor es null")
    void shouldReturnEmptyWhenNull() {
        // Arrange
        String value = null;
        
        // Act
        String result = TextHelper.getDefault(value);
        
        // Assert
        assertEquals("", result);
    }

    @Test
    @DisplayName("Debería hacer trim del valor")
    void shouldTrimValue() {
        // Arrange
        String value = "  Hello World  ";
        
        // Act
        String result = TextHelper.getDefaultWithTrim(value);
        
        // Assert
        assertEquals("Hello World", result);
    }

    @Test
    @DisplayName("Debería detectar string vacío")
    void shouldDetectEmptyString() {
        // Assert
        assertTrue(TextHelper.isEmpty(""));
        assertTrue(TextHelper.isEmpty("   "));
        assertTrue(TextHelper.isEmpty(null));
        assertFalse(TextHelper.isEmpty("Hello"));
    }
}