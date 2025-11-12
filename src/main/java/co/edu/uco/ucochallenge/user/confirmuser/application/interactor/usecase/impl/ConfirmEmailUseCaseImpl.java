package co.edu.uco.ucochallenge.user.confirmuser.application.interactor.usecase.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.crosscuting.exception.ValidationException;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.secondary.ports.repository.UserRepository;
import co.edu.uco.ucochallenge.user.confirmuser.application.interactor.usecase.ConfirmEmailUseCase;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class ConfirmEmailUseCaseImpl implements ConfirmEmailUseCase {

    private final UserRepository userRepository;
    
    // Métricas opcionales (se inyectan solo si están disponibles)
    private MeterRegistry meterRegistry;
    private Counter emailConfirmationCounter;
    private Counter emailConfirmationErrorCounter;

    public ConfirmEmailUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // Inyección opcional de MeterRegistry (solo si está disponible)
    @Autowired(required = false)
    public void setMeterRegistry(MeterRegistry meterRegistry) {
        if (meterRegistry != null) {
            this.meterRegistry = meterRegistry;
            this.emailConfirmationCounter = Counter.builder("user.email.confirmations.total")
                .description("Total de confirmaciones de email")
                .register(meterRegistry);
            this.emailConfirmationErrorCounter = Counter.builder("user.email.confirmations.errors")
                .description("Total de errores en confirmación de email")
                .register(meterRegistry);
        }
    }

    @Override
    @Timed(value = "user.email.confirmation", description = "Tiempo de confirmación de email")
    public Void execute(String code) {
        try {
            if (code == null || code.isBlank()) {
                if (meterRegistry != null) {
                    meterRegistry.counter("user.email.confirmations.errors", "error_type", "invalid_code").increment();
                } else if (emailConfirmationErrorCounter != null) {
                    emailConfirmationErrorCounter.increment();
                }
                throw new ValidationException("El código de confirmación es obligatorio");
            }

            Optional<UserEntity> userOpt = userRepository.findByEmailConfirmationToken(code);
            
            if (userOpt.isEmpty()) {
                if (meterRegistry != null) {
                    meterRegistry.counter("user.email.confirmations.errors", "error_type", "invalid_code").increment();
                } else if (emailConfirmationErrorCounter != null) {
                    emailConfirmationErrorCounter.increment();
                }
                throw new ValidationException("Código de confirmación inválido o no encontrado");
            }

            UserEntity user = userOpt.get();

            // Verificar si el código ha expirado
            if (user.getEmailConfirmationTokenExpiry() == null || 
                user.getEmailConfirmationTokenExpiry().isBefore(LocalDateTime.now())) {
                if (meterRegistry != null) {
                    meterRegistry.counter("user.email.confirmations.errors", "error_type", "expired_code").increment();
                } else if (emailConfirmationErrorCounter != null) {
                    emailConfirmationErrorCounter.increment();
                }
                throw new ValidationException("El código de confirmación ha expirado. Por favor solicita uno nuevo.");
            }

            // Verificar si ya está confirmado
            if (user.isEmailConfirmed()) {
                if (meterRegistry != null) {
                    meterRegistry.counter("user.email.confirmations.errors", "error_type", "already_confirmed").increment();
                } else if (emailConfirmationErrorCounter != null) {
                    emailConfirmationErrorCounter.increment();
                }
                throw new ValidationException("El correo electrónico ya está confirmado");
            }

            // Confirmar el email
            user.setEmailConfirmed(true);
            user.setEmailConfirmationToken(null);
            user.setEmailConfirmationTokenExpiry(null);
            userRepository.save(user);

            if (emailConfirmationCounter != null) {
                emailConfirmationCounter.increment();
            }

            return Void.returnVoid();
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            if (meterRegistry != null) {
                meterRegistry.counter("user.email.confirmations.errors", "error_type", "unknown").increment();
            } else if (emailConfirmationErrorCounter != null) {
                emailConfirmationErrorCounter.increment();
            }
            throw e;
        }
    }
}

