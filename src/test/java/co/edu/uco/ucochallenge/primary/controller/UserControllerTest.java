package co.edu.uco.ucochallenge.primary.controller;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.user.getusers.application.interactor.GetUsersInteractor;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.RegisterUserInteractor;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.UserOutputDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para UserController")
class UserControllerTest {

    @Mock
    private RegisterUserInteractor registerUserInteractor;

    @Mock
    private GetUsersInteractor getUsersInteractor;

    @InjectMocks
    private UserController controller;

    @Test
    @DisplayName("Debería registrar usuario y retornar 201 Created")
    void shouldRegisterUserAndReturn201() {
        // Arrange
        RegisterUserInputDTO dto = new RegisterUserInputDTO(
            UUID.randomUUID(),
            "123456",
            "Juan",
            "Carlos",
            "Pérez",
            "García",
            UUID.randomUUID(),
            "juan@example.com",
            "3001234567"
        );

        when(registerUserInteractor.execute(any())).thenReturn(Void.returnVoid());

        // Act
        ResponseEntity<String> result = controller.registerUser(dto);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertTrue(result.getBody().contains("successfully"));
        verify(registerUserInteractor, times(1)).execute(any());
    }

    @Test
    @DisplayName("Debería retornar lista de usuarios con status 200")
    void shouldReturnUsersWithStatus200() {
        // Arrange
        UserOutputDTO user = new UserOutputDTO(
            UUID.randomUUID(),
            "Cédula",
            "123456",
            "Juan",
            null,
            "Pérez",
            null,
            "Juan Pérez",
            "juan@example.com",
            "3001234567",
            null
        );

        Response<List<UserOutputDTO>> response = new Response<List<UserOutputDTO>>(true, List.of(user)) {};
        when(getUsersInteractor.execute(null)).thenReturn(response);

        // Act
        ResponseEntity<List<UserOutputDTO>> result = controller.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
    }
}
