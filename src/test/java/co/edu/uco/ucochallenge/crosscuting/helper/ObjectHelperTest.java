package co.edu.uco.ucochallenge.crosscuting.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para ObjectHelper")
class ObjectHelperTest {

    @Test
    @DisplayName("Debería detectar objeto null")
    void shouldDetectNullObject() {
        // Assert
        assertTrue(ObjectHelper.isNull(null));
        assertFalse(ObjectHelper.isNull("Not null"));
        assertFalse(ObjectHelper.isNull(123));
    }

    @Test
    @DisplayName("Debería retornar el objeto si no es null")
    void shouldReturnObjectWhenNotNull() {
        // Arrange
        String value = "Hello";

        // Act
        String result = ObjectHelper.getDefault(value, "Default");

        // Assert
        assertEquals("Hello", result);
    }

    @Test
    @DisplayName("Debería retornar el default si el objeto es null")
    void shouldReturnDefaultWhenObjectIsNull() {
        // Arrange
        String value = null;

        // Act
        String result = ObjectHelper.getDefault(value, "Default");

        // Assert
        assertEquals("Default", result);
    }

    @Test
    @DisplayName("Debería funcionar con diferentes tipos de objetos")
    void shouldWorkWithDifferentObjectTypes() {
        // Integer
        assertEquals(42, ObjectHelper.getDefault(null, 42));
        assertEquals(100, ObjectHelper.getDefault(100, 42));

        // Boolean
        assertTrue(ObjectHelper.getDefault(null, true));
        assertFalse(ObjectHelper.getDefault(false, true));
    }
}
