package co.edu.uco.ucochallenge.user.registeruser.application.interactor.impl;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.RegisterUserUseCase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para RegisterUserInteractorImpl")
class RegisterUserInteractorImplTest {

    @Mock
    private RegisterUserUseCase useCase;

    @InjectMocks
    private RegisterUserInteractorImpl interactor;

    @Test
    @DisplayName("Debería ejecutar el caso de uso correctamente")
    void shouldExecuteUseCaseCorrectly() {
        // Arrange
        RegisterUserInputDTO dto = new RegisterUserInputDTO(
            UUID.randomUUID(), "123456", "Juan", "Carlos",
            "Pérez", "García", UUID.randomUUID(),
            "juan@example.com", "3001234567"
        );

        when(useCase.execute(any())).thenReturn(Void.returnVoid());

        // Act
        Void result = interactor.execute(dto);

        // Assert
        assertNotNull(result);
        verify(useCase, times(1)).execute(any());
    }
}
