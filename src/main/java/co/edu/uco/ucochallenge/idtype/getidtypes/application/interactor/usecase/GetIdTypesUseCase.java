package co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.usecase;

import java.util.List;

import co.edu.uco.ucochallenge.application.interactor.usecase.UseCase;
import co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.dto.IdTypeOutputDTO;

public interface GetIdTypesUseCase extends UseCase<Void, List<IdTypeOutputDTO>> {
}
