package co.edu.uco.ucochallenge.user.getusers.application.interactor.usecase;

import java.util.List;

import co.edu.uco.ucochallenge.application.interactor.usecase.UseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.UserOutputDTO;

public interface GetUsersUseCase extends UseCase<Void, List<UserOutputDTO>> {
}
