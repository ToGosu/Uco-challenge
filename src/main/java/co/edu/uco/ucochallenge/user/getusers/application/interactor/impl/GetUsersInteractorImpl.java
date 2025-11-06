package co.edu.uco.ucochallenge.user.getusers.application.interactor.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.user.getusers.application.interactor.GetUsersInteractor;
import co.edu.uco.ucochallenge.user.getusers.application.interactor.usecase.GetUsersUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.UserOutputDTO;

@Service
public class GetUsersInteractorImpl implements GetUsersInteractor {

    private final GetUsersUseCase useCase;

    public GetUsersInteractorImpl(GetUsersUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public Response<List<UserOutputDTO>> execute(Void input) {
        List<UserOutputDTO> users = useCase.execute(null);
        return new Response<List<UserOutputDTO>>(true, users) {};
    }
}
