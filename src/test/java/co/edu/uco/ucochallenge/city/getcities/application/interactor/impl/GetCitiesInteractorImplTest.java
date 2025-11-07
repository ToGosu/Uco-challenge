package co.edu.uco.ucochallenge.city.getcities.application.interactor.impl;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.city.getcities.application.interactor.dto.CityOutputDTO;
import co.edu.uco.ucochallenge.city.getcities.application.interactor.usecase.GetCitiesUseCase;

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
@DisplayName("Tests para GetCitiesInteractorImpl")
class GetCitiesInteractorImplTest {

    @Mock
    private GetCitiesUseCase useCase;

    @InjectMocks
    private GetCitiesInteractorImpl interactor;

    @Test
    @DisplayName("Debería retornar Response con lista de ciudades")
    void shouldReturnResponseWithCities() {
        // Arrange
        CityOutputDTO city1 = new CityOutputDTO(UUID.randomUUID(), "Medellín");
        CityOutputDTO city2 = new CityOutputDTO(UUID.randomUUID(), "Bogotá");
        List<CityOutputDTO> cities = Arrays.asList(city1, city2);

        when(useCase.execute(null)).thenReturn(cities);

        // Act
        Response<List<CityOutputDTO>> response = interactor.execute(null);

        // Assert
        assertNotNull(response);
        assertTrue(response.isDataReturned());
        assertEquals(2, response.getData().size());
        verify(useCase, times(1)).execute(null);
    }

    @Test
    @DisplayName("Debería retornar Response con lista vacía")
    void shouldReturnResponseWithEmptyList() {
        // Arrange
        when(useCase.execute(null)).thenReturn(List.of());

        // Act
        Response<List<CityOutputDTO>> response = interactor.execute(null);

        // Assert
        assertNotNull(response);
        assertTrue(response.isDataReturned());
        assertTrue(response.getData().isEmpty());
    }
}
