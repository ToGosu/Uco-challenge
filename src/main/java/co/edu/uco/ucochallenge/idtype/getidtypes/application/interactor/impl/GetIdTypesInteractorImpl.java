package co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.GetIdTypesInteractor;
import co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.dto.IdTypeOutputDTO;
import co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.usecase.GetIdTypesUseCase;

@Service
public class GetIdTypesInteractorImpl implements GetIdTypesInteractor {

    private final GetIdTypesUseCase useCase;

    public GetIdTypesInteractorImpl(GetIdTypesUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public Response<List<IdTypeOutputDTO>> execute(Void input) {
        List<IdTypeOutputDTO> idTypes = useCase.execute(null);
        return new Response<List<IdTypeOutputDTO>>(true, idTypes) {};
    }
}
