package co.edu.uco.ucochallenge.user.confirmuser.application.interactor.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.user.confirmuser.application.interactor.ConfirmEmailInteractor;
import co.edu.uco.ucochallenge.user.confirmuser.application.interactor.usecase.ConfirmEmailUseCase;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ConfirmEmailInteractorImpl implements ConfirmEmailInteractor {

    private final ConfirmEmailUseCase useCase;

    public ConfirmEmailInteractorImpl(ConfirmEmailUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public Void execute(String token) {
        return useCase.execute(token);
    }
}

