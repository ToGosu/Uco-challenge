package co.edu.uco.ucochallenge.primary.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.GetIdTypesInteractor;
import co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.dto.IdTypeOutputDTO;

@RestController
@RequestMapping("/uco-challenge/api/v1/idtypes")
public class IdTypeController {

    private final GetIdTypesInteractor interactor;

    public IdTypeController(GetIdTypesInteractor interactor) {
        this.interactor = interactor;
    }

    @GetMapping
    public ResponseEntity<List<IdTypeOutputDTO>> getAllIdTypes() {
        Response<List<IdTypeOutputDTO>> response = interactor.execute(null);
        
        if (response.isDataReturned()) {
            return ResponseEntity.ok(response.getData());
        }
        
        return ResponseEntity.noContent().build();
    }
}
