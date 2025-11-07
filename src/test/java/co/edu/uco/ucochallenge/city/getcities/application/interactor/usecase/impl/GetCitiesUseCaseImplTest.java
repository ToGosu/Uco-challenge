package co.edu.uco.ucochallenge.city.getcities.application.interactor.usecase.impl;

import co.edu.uco.ucochallenge.city.getcities.application.interactor.dto.CityOutputDTO;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.StateEntity;
import co.edu.uco.ucochallenge.secondary.ports.repository.CityRepository;

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
@DisplayName("Tests para GetCitiesUseCaseImpl")
class GetCitiesUseCaseImplTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private GetCitiesUseCaseImpl getCitiesUseCase;

    private CityEntity city1;
    private CityEntity city2;

    @BeforeEach
    void setUp() {
        StateEntity state = new StateEntity();
        state.setId(UUID.randomUUID());
        state.setName("Antioquia");

        city1 = new CityEntity();
        city1.setId(UUID.randomUUID());
        city1.setName("Medellín");
        city1.setState(state);

        city2 = new CityEntity();
        city2.setId(UUID.randomUUID());
        city2.setName("Rionegro");
        city2.setState(state);
    }

    @Test
    @DisplayName("Debería retornar lista de ciudades")
    void shouldReturnListOfCities() {
        // Arrange
        when(cityRepository.findAll()).thenReturn(Arrays.asList(city1, city2));

        // Act
        List<CityOutputDTO> result = getCitiesUseCase.execute(null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Medellín", result.get(0).name());
        assertEquals("Rionegro", result.get(1).name());
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería retornar lista vacía si no hay ciudades")
    void shouldReturnEmptyListWhenNoCities() {
        // Arrange
        when(cityRepository.findAll()).thenReturn(List.of());

        // Act
        List<CityOutputDTO> result = getCitiesUseCase.execute(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería manejar ciudades con datos mínimos")
    void shouldHandleCitiesWithMinimalData() {
        // Arrange
        CityEntity minimalCity = new CityEntity();
        minimalCity.setId(UUID.randomUUID());
        minimalCity.setName("Test City");

        when(cityRepository.findAll()).thenReturn(List.of(minimalCity));

        // Act
        List<CityOutputDTO> result = getCitiesUseCase.execute(null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test City", result.get(0).name());
    }
}
