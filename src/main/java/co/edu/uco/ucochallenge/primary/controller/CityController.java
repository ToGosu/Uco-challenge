package co.edu.uco.ucochallenge.primary.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.city.getcities.application.interactor.GetCitiesInteractor;
import co.edu.uco.ucochallenge.city.getcities.application.interactor.dto.CityOutputDTO;

@RestController
@RequestMapping({"/uco-challenge/api/v1/cities", "/api/v1/cities", "/api/api/v1/cities"})
public class CityController {

    private final GetCitiesInteractor interactor;

    public CityController(GetCitiesInteractor interactor) {
        this.interactor = interactor;
    }

    @GetMapping
    public ResponseEntity<List<CityOutputDTO>> getAllCities() {
        Response<List<CityOutputDTO>> response = interactor.execute(null);
        
        if (response.isDataReturned()) {
            return ResponseEntity.ok(response.getData());
        }
        
        return ResponseEntity.noContent().build();
    }
}
