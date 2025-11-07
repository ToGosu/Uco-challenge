package co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para UserOutputDTO")
class UserOutputDTOTest {

    @Test
    @DisplayName("Debería crear DTO desde entidad")
    void shouldCreateDTOFromEntity() {
        // Arrange
        IdTypeEntity idType = new IdTypeEntity();
        idType.setName("Cédula");

        CityEntity city = new CityEntity();
        city.setId(UUID.randomUUID());
        city.setName("Medellín");

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setIdType(idType);
        user.setIdNumber("123456");
        user.setFirstName("Juan");
        user.setSecondName("Carlos");
        user.setFirstSurname("Pérez");
        user.setSecondSurname("García");
        user.setEmail("juan@example.com");
        user.setMobileNumber("3001234567");
        user.setHomeCity(city);

        // Act
        UserOutputDTO dto = UserOutputDTO.from(user);

        // Assert
        assertNotNull(dto);
        assertEquals(user.getId(), dto.id());
        assertEquals("Cédula", dto.idType());
        assertEquals("123456", dto.idNumber());
        assertEquals("Juan Carlos Pérez García", dto.fullName());
        assertEquals("juan@example.com", dto.email());
    }

    @Test
    @DisplayName("Debería retornar null si la entidad es null")
    void shouldReturnNullWhenEntityIsNull() {
        // Act
        UserOutputDTO dto = UserOutputDTO.from(null);

        // Assert
        assertNull(dto);
    }

    @Test
    @DisplayName("Debería construir nombre completo correctamente")
    void shouldBuildFullNameCorrectly() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setFirstName("Ana");
        user.setFirstSurname("López");

        // Act
        UserOutputDTO dto = UserOutputDTO.from(user);

        // Assert
        assertEquals("Ana López", dto.fullName());
    }
}