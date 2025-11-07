package co.edu.uco.ucochallenge.primary.controller;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.GetIdTypesInteractor;
import co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.dto.IdTypeOutputDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para IdTypeController")
class IdTypeControllerTest {

    @Mock
    private GetIdTypesInteractor interactor;

    @InjectMocks
    private IdTypeController controller;

    @Test
    @DisplayName("Debería retornar lista de tipos de ID con status 200")
    void shouldReturnIdTypesWithStatus200() {
        // Arrange
        IdTypeOutputDTO idType1 = new IdTypeOutputDTO(UUID.randomUUID(), "Cédula");
        IdTypeOutputDTO idType2 = new IdTypeOutputDTO(UUID.randomUUID(), "Pasaporte");
        List<IdTypeOutputDTO> idTypes = Arrays.asList(idType1, idType2);

        Response<List<IdTypeOutputDTO>> response = new Response<List<IdTypeOutputDTO>>(true, idTypes) {};
        when(interactor.execute(null)).thenReturn(response);

        // Act
        ResponseEntity<List<IdTypeOutputDTO>> result = controller.getAllIdTypes();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        verify(interactor, times(1)).execute(null);
    }
}
