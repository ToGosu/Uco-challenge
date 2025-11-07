package co.edu.uco.ucochallenge.user.getusers.application.interactor.impl;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.user.getusers.application.interactor.usecase.GetUsersUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.UserOutputDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para GetUsersInteractorImpl")
class GetUsersInteractorImplTest {

    @Mock
    private GetUsersUseCase useCase;

    @InjectMocks
    private GetUsersInteractorImpl interactor;

    @Test
    @DisplayName("Debería retornar Response con lista de usuarios")
    void shouldReturnResponseWithUsers() {
        // Arrange
        UserOutputDTO user1 = new UserOutputDTO(
            UUID.randomUUID(), "Cédula", "123456", 
            "Juan", null, "Pérez", null, "Juan Pérez",
            "juan@example.com", "3001234567", null
        );

        when(useCase.execute(null)).thenReturn(List.of(user1));

        // Act
        Response<List<UserOutputDTO>> response = interactor.execute(null);

        // Assert
        assertNotNull(response);
        assertTrue(response.isDataReturned());
        assertEquals(1, response.getData().size());
        verify(useCase, times(1)).execute(null);
    }
}
