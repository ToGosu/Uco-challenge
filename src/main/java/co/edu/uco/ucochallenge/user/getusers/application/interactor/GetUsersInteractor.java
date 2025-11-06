package co.edu.uco.ucochallenge.user.getusers.application.interactor;

import java.util.List;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.application.interactor.Interactor;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.UserOutputDTO;

public interface GetUsersInteractor extends Interactor<Void, Response<List<UserOutputDTO>>> {
}
