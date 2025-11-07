package co.edu.uco.ucochallenge.city.getcities.application.interactor.dto;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para CityOutputDTO")
class CityOutputDTOTest {

    @Test
    @DisplayName("Debería crear DTO desde entidad")
    void shouldCreateDTOFromEntity() {
        // Arrange
        CityEntity city = new CityEntity();
        city.setId(UUID.randomUUID());
        city.setName("Medellín");

        // Act
        CityOutputDTO dto = CityOutputDTO.fromSimple(city);

        // Assert
        assertNotNull(dto);
        assertEquals(city.getId(), dto.id());
        assertEquals("Medellín", dto.name());
    }

    @Test
    @DisplayName("Debería retornar null si la entidad es null")
    void shouldReturnNullWhenEntityIsNull() {
        // Act
        CityOutputDTO dto = CityOutputDTO.fromSimple(null);

        // Assert
        assertNull(dto);
    }
}
