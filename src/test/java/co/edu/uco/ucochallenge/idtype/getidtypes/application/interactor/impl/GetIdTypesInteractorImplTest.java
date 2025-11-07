package co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.impl;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.dto.IdTypeOutputDTO;
import co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.usecase.GetIdTypesUseCase;

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
@DisplayName("Tests para GetIdTypesInteractorImpl")
class GetIdTypesInteractorImplTest {

    @Mock
    private GetIdTypesUseCase useCase;

    @InjectMocks
    private GetIdTypesInteractorImpl interactor;

    @Test
    @DisplayName("Debería retornar Response con lista de tipos de ID")
    void shouldReturnResponseWithIdTypes() {
        // Arrange
        IdTypeOutputDTO idType1 = new IdTypeOutputDTO(UUID.randomUUID(), "Cédula");
        IdTypeOutputDTO idType2 = new IdTypeOutputDTO(UUID.randomUUID(), "Pasaporte");
        List<IdTypeOutputDTO> idTypes = Arrays.asList(idType1, idType2);

        when(useCase.execute(null)).thenReturn(idTypes);

        // Act
        Response<List<IdTypeOutputDTO>> response = interactor.execute(null);

        // Assert
        assertNotNull(response);
        assertTrue(response.isDataReturned());
        assertEquals(2, response.getData().size());
        verify(useCase, times(1)).execute(null);
    }
}
