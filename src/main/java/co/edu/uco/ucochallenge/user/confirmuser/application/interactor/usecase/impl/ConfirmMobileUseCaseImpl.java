package co.edu.uco.ucochallenge.user.confirmuser.application.interactor.usecase.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.crosscuting.exception.ValidationException;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.secondary.ports.repository.UserRepository;
import co.edu.uco.ucochallenge.user.confirmuser.application.interactor.usecase.ConfirmMobileUseCase;

@Service
public class ConfirmMobileUseCaseImpl implements ConfirmMobileUseCase {

    private final UserRepository userRepository;

    public ConfirmMobileUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Void execute(String code) {
        if (code == null || code.isBlank()) {
            throw new ValidationException("El código de confirmación es obligatorio");
        }

        Optional<UserEntity> userOpt = userRepository.findByMobileConfirmationToken(code);
        
        if (userOpt.isEmpty()) {
            throw new ValidationException("Código de confirmación inválido o no encontrado");
        }

        UserEntity user = userOpt.get();

        // Verificar si el token ha expirado
        if (user.getMobileConfirmationTokenExpiry() == null || 
            user.getMobileConfirmationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new ValidationException("El código de confirmación ha expirado. Por favor solicita uno nuevo.");
        }

        // Verificar si ya está confirmado
        if (user.isMobileNumberConfirmed()) {
            throw new ValidationException("El número de celular ya está confirmado");
        }

        // Confirmar el número de celular
        user.setMobileNumberConfirmed(true);
        user.setMobileConfirmationToken(null);
        user.setMobileConfirmationTokenExpiry(null);
        userRepository.save(user);

        return Void.returnVoid();
    }
}

