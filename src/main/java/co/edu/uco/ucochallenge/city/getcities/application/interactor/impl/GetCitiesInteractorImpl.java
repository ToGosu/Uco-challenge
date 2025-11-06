package co.edu.uco.ucochallenge.city.getcities.application.interactor.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.city.getcities.application.interactor.GetCitiesInteractor;
import co.edu.uco.ucochallenge.city.getcities.application.interactor.dto.CityOutputDTO;
import co.edu.uco.ucochallenge.city.getcities.application.interactor.usecase.GetCitiesUseCase;

@Service
public class GetCitiesInteractorImpl implements GetCitiesInteractor {

    private final GetCitiesUseCase useCase;

    public GetCitiesInteractorImpl(GetCitiesUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public Response<List<CityOutputDTO>> execute(Void input) {
        List<CityOutputDTO> cities = useCase.execute(null);
        return new Response<List<CityOutputDTO>>(true, cities) {};
    }
}
