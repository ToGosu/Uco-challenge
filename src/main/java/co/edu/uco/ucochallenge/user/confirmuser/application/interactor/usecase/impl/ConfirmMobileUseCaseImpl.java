package co.edu.uco.ucochallenge.user.confirmuser.application.interactor.usecase.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.crosscuting.exception.ValidationException;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.secondary.ports.repository.UserRepository;
import co.edu.uco.ucochallenge.user.confirmuser.application.interactor.usecase.ConfirmMobileUseCase;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class ConfirmMobileUseCaseImpl implements ConfirmMobileUseCase {

    private final UserRepository userRepository;
    
    // Métricas opcionales (se inyectan solo si están disponibles)
    private MeterRegistry meterRegistry;
    private Counter mobileConfirmationCounter;
    private Counter mobileConfirmationErrorCounter;

    public ConfirmMobileUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // Inyección opcional de MeterRegistry (solo si está disponible)
    @Autowired(required = false)
    public void setMeterRegistry(MeterRegistry meterRegistry) {
        if (meterRegistry != null) {
            this.meterRegistry = meterRegistry;
            this.mobileConfirmationCounter = Counter.builder("user.mobile.confirmations.total")
                .description("Total de confirmaciones de móvil")
                .register(meterRegistry);
            this.mobileConfirmationErrorCounter = Counter.builder("user.mobile.confirmations.errors")
                .description("Total de errores en confirmación de móvil")
                .register(meterRegistry);
        }
    }

    @Override
    @Timed(value = "user.mobile.confirmation", description = "Tiempo de confirmación de móvil")
    public Void execute(String code) {
        try {
            if (code == null || code.isBlank()) {
                if (meterRegistry != null) {
                    meterRegistry.counter("user.mobile.confirmations.errors", "error_type", "invalid_code").increment();
                } else if (mobileConfirmationErrorCounter != null) {
                    mobileConfirmationErrorCounter.increment();
                }
                throw new ValidationException("El código de confirmación es obligatorio");
            }

            Optional<UserEntity> userOpt = userRepository.findByMobileConfirmationToken(code);
            
            if (userOpt.isEmpty()) {
                if (meterRegistry != null) {
                    meterRegistry.counter("user.mobile.confirmations.errors", "error_type", "invalid_code").increment();
                } else if (mobileConfirmationErrorCounter != null) {
                    mobileConfirmationErrorCounter.increment();
                }
                throw new ValidationException("Código de confirmación inválido o no encontrado");
            }

            UserEntity user = userOpt.get();

            // Verificar si el token ha expirado
            if (user.getMobileConfirmationTokenExpiry() == null || 
                user.getMobileConfirmationTokenExpiry().isBefore(LocalDateTime.now())) {
                if (meterRegistry != null) {
                    meterRegistry.counter("user.mobile.confirmations.errors", "error_type", "expired_code").increment();
                } else if (mobileConfirmationErrorCounter != null) {
                    mobileConfirmationErrorCounter.increment();
                }
                throw new ValidationException("El código de confirmación ha expirado. Por favor solicita uno nuevo.");
            }

            // Verificar si ya está confirmado
            if (user.isMobileNumberConfirmed()) {
                if (meterRegistry != null) {
                    meterRegistry.counter("user.mobile.confirmations.errors", "error_type", "already_confirmed").increment();
                } else if (mobileConfirmationErrorCounter != null) {
                    mobileConfirmationErrorCounter.increment();
                }
                throw new ValidationException("El número de celular ya está confirmado");
            }

            // Confirmar el número de celular
            user.setMobileNumberConfirmed(true);
            user.setMobileConfirmationToken(null);
            user.setMobileConfirmationTokenExpiry(null);
            userRepository.save(user);

            if (mobileConfirmationCounter != null) {
                mobileConfirmationCounter.increment();
            }

            return Void.returnVoid();
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            if (meterRegistry != null) {
                meterRegistry.counter("user.mobile.confirmations.errors", "error_type", "unknown").increment();
            } else if (mobileConfirmationErrorCounter != null) {
                mobileConfirmationErrorCounter.increment();
            }
            throw e;
        }
    }
}

