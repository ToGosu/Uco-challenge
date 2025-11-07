package co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para RegisterUserInputDTO")
class RegisterUserInputDTOTest {

    @Test
    @DisplayName("Debería normalizar correctamente los datos")
    void shouldNormalizeDataCorrectly() {
        // Arrange
        UUID idType = UUID.randomUUID();
        UUID homeCity = UUID.randomUUID();

        // Act
        RegisterUserInputDTO dto = RegisterUserInputDTO.normalize(
            idType,
            "  123456  ",
            "  Juan  ",
            "  Carlos  ",
            "  Pérez  ",
            "  García  ",
            homeCity,
            "  juan@example.com  ",
            "  3001234567  "
        );

        // Assert
        assertEquals("123456", dto.idNumber());
        assertEquals("Juan", dto.firstName());
        assertEquals("Carlos", dto.secondName());
        assertEquals("Pérez", dto.firstSurname());
        assertEquals("García", dto.secondSurname());
        assertEquals("juan@example.com", dto.email());
        assertEquals("3001234567", dto.mobileNumber());
    }

    @Test
    @DisplayName("Debería manejar valores null correctamente")
    void shouldHandleNullValuesCorrectly() {
        // Act
        RegisterUserInputDTO dto = RegisterUserInputDTO.normalize(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );

        // Assert
        assertNotNull(dto);
        assertEquals("", dto.idNumber());
        assertEquals("", dto.firstName());
        assertEquals("", dto.email());
    }

    @Test
    @DisplayName("Debería crear DTO con todos los campos")
    void shouldCreateDTOWithAllFields() {
        // Arrange
        UUID idType = UUID.randomUUID();
        UUID homeCity = UUID.randomUUID();

        // Act
        RegisterUserInputDTO dto = new RegisterUserInputDTO(
            idType,
            "123456",
            "Juan",
            "Carlos",
            "Pérez",
            "García",
            homeCity,
            "juan@example.com",
            "3001234567"
        );

        // Assert
        assertEquals(idType, dto.idType());
        assertEquals("123456", dto.idNumber());
        assertEquals("Juan", dto.firstName());
        assertEquals("Carlos", dto.secondName());
        assertEquals("Pérez", dto.firstSurname());
        assertEquals("García", dto.secondSurname());
        assertEquals(homeCity, dto.homeCity());
        assertEquals("juan@example.com", dto.email());
        assertEquals("3001234567", dto.mobileNumber());
    }
}

