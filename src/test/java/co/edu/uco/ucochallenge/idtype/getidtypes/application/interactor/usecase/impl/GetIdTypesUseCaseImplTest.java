package co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.usecase.impl;

import co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.dto.IdTypeOutputDTO;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;
import co.edu.uco.ucochallenge.secondary.ports.repository.IdTypeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para GetIdTypesUseCaseImpl")
class GetIdTypesUseCaseImplTest {

    @Mock
    private IdTypeRepository idTypeRepository;

    @InjectMocks
    private GetIdTypesUseCaseImpl getIdTypesUseCase;

    private IdTypeEntity idType1;
    private IdTypeEntity idType2;

    @BeforeEach
    void setUp() {
        idType1 = new IdTypeEntity();
        idType1.setId(UUID.randomUUID());
        idType1.setName("Cédula de Ciudadanía");

        idType2 = new IdTypeEntity();
        idType2.setId(UUID.randomUUID());
        idType2.setName("Pasaporte");
    }

    @Test
    @DisplayName("Debería retornar lista de tipos de identificación")
    void shouldReturnListOfIdTypes() {
        // Arrange
        when(idTypeRepository.findAll()).thenReturn(Arrays.asList(idType1, idType2));

        // Act
        List<IdTypeOutputDTO> result = getIdTypesUseCase.execute(null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Cédula de Ciudadanía", result.get(0).name());
        assertEquals("Pasaporte", result.get(1).name());
        verify(idTypeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería retornar lista vacía si no hay tipos de ID")
    void shouldReturnEmptyListWhenNoIdTypes() {
        // Arrange
        when(idTypeRepository.findAll()).thenReturn(List.of());

        // Act
        List<IdTypeOutputDTO> result = getIdTypesUseCase.execute(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(idTypeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería convertir correctamente las entidades a DTOs")
    void shouldConvertEntitiesToDTOsCorrectly() {
        // Arrange
        when(idTypeRepository.findAll()).thenReturn(List.of(idType1));

        // Act
        List<IdTypeOutputDTO> result = getIdTypesUseCase.execute(null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(idType1.getId(), result.get(0).id());
        assertEquals(idType1.getName(), result.get(0).name());
    }
}
