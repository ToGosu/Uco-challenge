package co.edu.uco.ucochallenge.city.getcities.application.interactor;

import java.util.List;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.application.interactor.Interactor;
import co.edu.uco.ucochallenge.city.getcities.application.interactor.dto.CityOutputDTO;

public interface GetCitiesInteractor extends Interactor<Void, Response<List<CityOutputDTO>>> {
}
