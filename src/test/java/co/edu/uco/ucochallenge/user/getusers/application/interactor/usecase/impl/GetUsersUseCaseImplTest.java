package co.edu.uco.ucochallenge.user.getusers.application.interactor.usecase.impl;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.secondary.ports.repository.UserRepository;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.UserOutputDTO;

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
@DisplayName("Tests para GetUsersUseCaseImpl")
class GetUsersUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUsersUseCaseImpl getUsersUseCase;

    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    void setUp() {
        IdTypeEntity idType = new IdTypeEntity();
        idType.setId(UUID.randomUUID());
        idType.setName("Cédula");

        CityEntity city = new CityEntity();
        city.setId(UUID.randomUUID());
        city.setName("Bogotá");

        user1 = new UserEntity();
        user1.setId(UUID.randomUUID());
        user1.setIdType(idType);
        user1.setIdNumber("123456");
        user1.setFirstName("Juan");
        user1.setFirstSurname("Pérez");
        user1.setEmail("juan@example.com");
        user1.setHomeCity(city);

        user2 = new UserEntity();
        user2.setId(UUID.randomUUID());
        user2.setIdType(idType);
        user2.setIdNumber("789012");
        user2.setFirstName("María");
        user2.setFirstSurname("García");
        user2.setEmail("maria@example.com");
        user2.setHomeCity(city);
    }

    @Test
    @DisplayName("Debería retornar lista de usuarios")
    void shouldReturnListOfUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<UserOutputDTO> result = getUsersUseCase.execute(null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).firstName());
        assertEquals("María", result.get(1).firstName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería retornar lista vacía si no hay usuarios")
    void shouldReturnEmptyListWhenNoUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of());

        // Act
        List<UserOutputDTO> result = getUsersUseCase.execute(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}