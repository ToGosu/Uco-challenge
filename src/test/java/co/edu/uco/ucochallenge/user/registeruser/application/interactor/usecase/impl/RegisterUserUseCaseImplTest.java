package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.impl;

import co.edu.uco.ucochallenge.crosscuting.exception.ValidationException;
import co.edu.uco.ucochallenge.crosscuting.exception.user.UserAlreadyExistsException;
import co.edu.uco.ucochallenge.secondary.adapters.client.NotificationClient;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.secondary.ports.repository.CityRepository;
import co.edu.uco.ucochallenge.secondary.ports.repository.IdTypeRepository;
import co.edu.uco.ucochallenge.secondary.ports.repository.UserRepository;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para RegisterUserUseCaseImpl")
class RegisterUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private IdTypeRepository idTypeRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private RegisterUserUseCaseImpl registerUserUseCase;

    private RegisterUserDomain validDomain;
    private UUID idTypeId;
    private UUID cityId;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba
        idTypeId = UUID.randomUUID();
        cityId = UUID.randomUUID();

        validDomain = new RegisterUserDomain(
            idTypeId,
            "1234567890",
            "Juan",
            "Carlos",
            "Pérez",
            "García",
            cityId,
            "juan.perez@example.com",
            "3001234567"
        );
    }

    @Test
    @DisplayName("Debería registrar usuario exitosamente")
    void shouldRegisterUserSuccessfully() {
        // Arrange
        when(userRepository.existsByIdNumber(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByMobileNumber(any())).thenReturn(false);

        IdTypeEntity idType = new IdTypeEntity();
        idType.setId(idTypeId);
        when(idTypeRepository.findById(idTypeId)).thenReturn(Optional.of(idType));

        CityEntity city = new CityEntity();
        city.setId(cityId);
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));

        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        var result = registerUserUseCase.execute(validDomain);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(notificationClient, times(1)).sendWelcomeEmail(anyString(), anyString());
        verify(notificationClient, times(1)).sendWelcomeSms(anyString(), anyString());
    }

    @Test
    @DisplayName("Debería lanzar excepción si el número de ID ya existe")
    void shouldThrowExceptionWhenIdNumberExists() {
        // Arrange
        when(userRepository.existsByIdNumber(validDomain.getIdNumber())).thenReturn(true);

        // Act & Assert
        UserAlreadyExistsException ex = assertThrows(
            UserAlreadyExistsException.class,
            () -> registerUserUseCase.execute(validDomain)
        );

        assertTrue(ex.getMessage().contains("número de identificación ya está registrado"));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería lanzar excepción si el email ya existe")
    void shouldThrowExceptionWhenEmailExists() {
        // Arrange
        when(userRepository.existsByIdNumber(any())).thenReturn(false);
        when(userRepository.existsByEmail(validDomain.getEmail())).thenReturn(true);

        // Act & Assert
        UserAlreadyExistsException ex = assertThrows(
            UserAlreadyExistsException.class,
            () -> registerUserUseCase.execute(validDomain)
        );

        assertTrue(ex.getMessage().contains("correo electrónico ya está registrado"));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería lanzar excepción si el tipo de ID no existe")
    void shouldThrowExceptionWhenIdTypeNotFound() {
        // Arrange
        when(userRepository.existsByIdNumber(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByMobileNumber(any())).thenReturn(false);
        when(idTypeRepository.findById(idTypeId)).thenReturn(Optional.empty());

        // Act & Assert
        ValidationException ex = assertThrows(
            ValidationException.class,
            () -> registerUserUseCase.execute(validDomain)
        );

        assertTrue(ex.getMessage().contains("Tipo de identificación no encontrado"));
    }

    @Test
    @DisplayName("Debería lanzar excepción si la ciudad no existe")
    void shouldThrowExceptionWhenCityNotFound() {
        // Arrange
        when(userRepository.existsByIdNumber(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByMobileNumber(any())).thenReturn(false);

        IdTypeEntity idType = new IdTypeEntity();
        when(idTypeRepository.findById(idTypeId)).thenReturn(Optional.of(idType));
        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        // Act & Assert
        ValidationException ex = assertThrows(
            ValidationException.class,
            () -> registerUserUseCase.execute(validDomain)
        );

        assertTrue(ex.getMessage().contains("Ciudad no encontrada"));
    }

    @Test
    @DisplayName("Debería validar email inválido")
    void shouldRejectInvalidEmail() {
        // Arrange
        validDomain.setEmail("invalid-email");

        // Act & Assert
        assertThrows(ValidationException.class, () -> registerUserUseCase.execute(validDomain));
    }

    @Test
    @DisplayName("Debería validar número de celular inválido")
    void shouldRejectInvalidMobileNumber() {
        // Arrange
        validDomain.setMobileNumber("123"); // Muy corto

        // Act & Assert
        assertThrows(ValidationException.class, () -> registerUserUseCase.execute(validDomain));
    }

    @Test
    @DisplayName("Debería continuar si fallan las notificaciones")
    void shouldContinueWhenNotificationsFail() {
        // Arrange
        when(userRepository.existsByIdNumber(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByMobileNumber(any())).thenReturn(false);

        IdTypeEntity idType = new IdTypeEntity();
        when(idTypeRepository.findById(idTypeId)).thenReturn(Optional.of(idType));

        CityEntity city = new CityEntity();
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));

        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Simular fallo en notificaciones
        doThrow(new RuntimeException("Email service down"))
            .when(notificationClient).sendWelcomeEmail(anyString(), anyString());

        // Act - No debe lanzar excepción
        assertDoesNotThrow(() -> registerUserUseCase.execute(validDomain));

        // Assert
        verify(userRepository, times(1)).save(any());
    }
}