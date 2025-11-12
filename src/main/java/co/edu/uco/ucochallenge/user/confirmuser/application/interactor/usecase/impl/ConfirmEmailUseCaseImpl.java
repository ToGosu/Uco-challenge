package co.edu.uco.ucochallenge.user.confirmuser.application.interactor.usecase.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.crosscuting.exception.ValidationException;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.secondary.ports.repository.UserRepository;
import co.edu.uco.ucochallenge.user.confirmuser.application.interactor.usecase.ConfirmEmailUseCase;

@Service
public class ConfirmEmailUseCaseImpl implements ConfirmEmailUseCase {

    private final UserRepository userRepository;

    public ConfirmEmailUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Void execute(String token) {
        if (token == null || token.isBlank()) {
            throw new ValidationException("El token de confirmación es obligatorio");
        }

        Optional<UserEntity> userOpt = userRepository.findByEmailConfirmationToken(token);
        
        if (userOpt.isEmpty()) {
            throw new ValidationException("Token de confirmación inválido o no encontrado");
        }

        UserEntity user = userOpt.get();

        // Verificar si el token ha expirado
        if (user.getEmailConfirmationTokenExpiry() == null || 
            user.getEmailConfirmationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new ValidationException("El token de confirmación ha expirado. Por favor solicita uno nuevo.");
        }

        // Verificar si ya está confirmado
        if (user.isEmailConfirmed()) {
            throw new ValidationException("El correo electrónico ya está confirmado");
        }

        // Confirmar el email
        user.setEmailConfirmed(true);
        user.setEmailConfirmationToken(null);
        user.setEmailConfirmationTokenExpiry(null);
        userRepository.save(user);

        return Void.returnVoid();
    }
}

