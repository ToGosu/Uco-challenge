package co.edu.uco.ucochallenge.crosscuting.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para UUIDHelper")
class UUIDHelperTest {

    @Test
    @DisplayName("Debería retornar UUID con ceros por defecto")
    void shouldReturnDefaultUUID() {
        // Act
        UUID result = UUIDHelper.getDefault();
        
        // Assert
        assertNotNull(result);
        assertEquals(new UUID(0L, 0L), result);
        assertEquals("00000000-0000-0000-0000-000000000000", result.toString());
    }

    @Test
    @DisplayName("Debería retornar el mismo UUID si no es null")
    void shouldReturnSameUUIDWhenNotNull() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        
        // Act
        UUID result = UUIDHelper.getDefault(uuid);
        
        // Assert
        assertEquals(uuid, result);
    }

    @Test
    @DisplayName("Debería retornar UUID por defecto si es null")
    void shouldReturnDefaultWhenNull() {
        // Act
        UUID result = UUIDHelper.getDefault(null);
        
        // Assert
        assertEquals(new UUID(0L, 0L), result);
    }

    @Test
    @DisplayName("Debería crear UUID desde string válido")
    void shouldCreateUUIDFromValidString() {
        // Arrange
        String uuidString = "123e4567-e89b-12d3-a456-426614174000";
        
        // Act
        UUID result = UUIDHelper.getFromString(uuidString);
        
        // Assert
        assertEquals(UUID.fromString(uuidString), result);
    }

    @Test
    @DisplayName("Debería retornar UUID por defecto si string está vacío")
    void shouldReturnDefaultWhenStringEmpty() {
        // Act
        UUID result1 = UUIDHelper.getFromString("");
        UUID result2 = UUIDHelper.getFromString(null);
        
        // Assert
        assertEquals(new UUID(0L, 0L), result1);
        assertEquals(new UUID(0L, 0L), result2);
    }
}