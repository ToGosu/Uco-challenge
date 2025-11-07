package co.edu.uco.ucochallenge.primary.controller;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.city.getcities.application.interactor.GetCitiesInteractor;
import co.edu.uco.ucochallenge.city.getcities.application.interactor.dto.CityOutputDTO;

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
@DisplayName("Tests para CityController")
class CityControllerTest {

    @Mock
    private GetCitiesInteractor interactor;

    @InjectMocks
    private CityController controller;

    @Test
    @DisplayName("Debería retornar lista de ciudades con status 200")
    void shouldReturnCitiesWithStatus200() {
        // Arrange
        CityOutputDTO city1 = new CityOutputDTO(UUID.randomUUID(), "Medellín");
        CityOutputDTO city2 = new CityOutputDTO(UUID.randomUUID(), "Bogotá");
        List<CityOutputDTO> cities = Arrays.asList(city1, city2);

        Response<List<CityOutputDTO>> response = new Response<List<CityOutputDTO>>(true, cities) {};
        when(interactor.execute(null)).thenReturn(response);

        // Act
        ResponseEntity<List<CityOutputDTO>> result = controller.getAllCities();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        verify(interactor, times(1)).execute(null);
    }

    @Test
    @DisplayName("Debería retornar 204 No Content si no hay ciudades")
    void shouldReturn204WhenNoCities() {
        // Arrange
        Response<List<CityOutputDTO>> response = new Response<List<CityOutputDTO>>(false, null) {};
        when(interactor.execute(null)).thenReturn(response);

        // Act
        ResponseEntity<List<CityOutputDTO>> result = controller.getAllCities();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }
}
