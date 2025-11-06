package co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor;

import java.util.List;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.application.interactor.Interactor;
import co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.dto.IdTypeOutputDTO;

public interface GetIdTypesInteractor extends Interactor<Void, Response<List<IdTypeOutputDTO>>> {
}