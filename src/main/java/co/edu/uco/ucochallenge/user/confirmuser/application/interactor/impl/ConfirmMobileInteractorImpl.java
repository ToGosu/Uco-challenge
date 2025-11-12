package co.edu.uco.ucochallenge.user.confirmuser.application.interactor.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.user.confirmuser.application.interactor.ConfirmMobileInteractor;
import co.edu.uco.ucochallenge.user.confirmuser.application.interactor.usecase.ConfirmMobileUseCase;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ConfirmMobileInteractorImpl implements ConfirmMobileInteractor {

    private final ConfirmMobileUseCase useCase;

    public ConfirmMobileInteractorImpl(ConfirmMobileUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public Void execute(String code) {
        return useCase.execute(code);
    }
}

