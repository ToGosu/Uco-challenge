package co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.dto;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para IdTypeOutputDTO")
class IdTypeOutputDTOTest {

    @Test
    @DisplayName("Debería crear DTO desde entidad")
    void shouldCreateDTOFromEntity() {
        // Arrange
        IdTypeEntity idType = new IdTypeEntity();
        idType.setId(UUID.randomUUID());
        idType.setName("Cédula de Ciudadanía");

        // Act
        IdTypeOutputDTO dto = IdTypeOutputDTO.from(idType);

        // Assert
        assertNotNull(dto);
        assertEquals(idType.getId(), dto.id());
        assertEquals("Cédula de Ciudadanía", dto.name());
    }

    @Test
    @DisplayName("Debería retornar null si la entidad es null")
    void shouldReturnNullWhenEntityIsNull() {
        // Act
        IdTypeOutputDTO dto = IdTypeOutputDTO.from(null);

        // Assert
        assertNull(dto);
    }

    @Test
    @DisplayName("fromSimple debería funcionar igual que from")
    void fromSimpleShouldWorkLikeFrom() {
        // Arrange
        IdTypeEntity idType = new IdTypeEntity();
        idType.setId(UUID.randomUUID());
        idType.setName("Pasaporte");

        // Act
        IdTypeOutputDTO dto1 = IdTypeOutputDTO.from(idType);
        IdTypeOutputDTO dto2 = IdTypeOutputDTO.fromSimple(idType);

        // Assert
        assertEquals(dto1.id(), dto2.id());
        assertEquals(dto1.name(), dto2.name());
    }
}
