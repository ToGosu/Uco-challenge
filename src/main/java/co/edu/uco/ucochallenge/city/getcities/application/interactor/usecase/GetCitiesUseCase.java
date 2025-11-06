package co.edu.uco.ucochallenge.city.getcities.application.interactor.usecase;

import java.util.List;

import co.edu.uco.ucochallenge.application.interactor.usecase.UseCase;
import co.edu.uco.ucochallenge.city.getcities.application.interactor.dto.CityOutputDTO;

public interface GetCitiesUseCase extends UseCase<Void, List<CityOutputDTO>> {
}
