package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.crosscuting.exception.ValidationException;
import co.edu.uco.ucochallenge.crosscuting.exception.user.UserAlreadyExistsException;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.secondary.adapters.client.NotificationClient;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.secondary.ports.repository.CityRepository;
import co.edu.uco.ucochallenge.secondary.ports.repository.IdTypeRepository;
import co.edu.uco.ucochallenge.secondary.ports.repository.UserRepository;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.RegisterUserUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Service
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final IdTypeRepository idTypeRepository;
    private final CityRepository cityRepository;
    private final NotificationClient notificationClient;
    
    // Métricas opcionales (se inyectan solo si están disponibles)
    private MeterRegistry meterRegistry;
    private Counter userRegistrationCounter;
    private Counter userRegistrationErrorCounter;
    private Timer userRegistrationTimer;

    public RegisterUserUseCaseImpl(
            UserRepository userRepository,
            IdTypeRepository idTypeRepository,
            CityRepository cityRepository,
            NotificationClient notificationClient) {
        this.userRepository = userRepository;
        this.idTypeRepository = idTypeRepository;
        this.cityRepository = cityRepository;
        this.notificationClient = notificationClient;
    }
    
    // Inyección opcional de MeterRegistry (solo si está disponible)
    @Autowired(required = false)
    public void setMeterRegistry(MeterRegistry meterRegistry) {
        if (meterRegistry != null) {
            this.meterRegistry = meterRegistry;
            this.userRegistrationCounter = Counter.builder("user.registrations.total")
                .description("Total de usuarios registrados")
                .tag("operation", "register")
                .register(meterRegistry);
            this.userRegistrationErrorCounter = Counter.builder("user.registrations.errors")
                .description("Total de errores en registro de usuarios")
                .tag("operation", "register")
                .register(meterRegistry);
            this.userRegistrationTimer = Timer.builder("user.registration.duration")
                .description("Tiempo de ejecución del registro de usuario")
                .register(meterRegistry);
        }
    }

    @Override
    @Timed(value = "user.registration", description = "Tiempo de registro de usuario")
    public Void execute(final RegisterUserDomain domain) {
        Timer.Sample sample = userRegistrationTimer != null ? Timer.start() : null;
        try {

        validateUserDoesNotExist(domain);
        validateRequiredFields(domain);
        setDefaultsIfNeeded(domain);

        IdTypeEntity idTypeEntity = idTypeRepository.findById(domain.getIdType())
                .orElseThrow(() -> new ValidationException(
                        "Tipo de identificación no encontrado: " + domain.getIdType()));

        CityEntity cityEntity = cityRepository.findById(domain.getHomeCity())
                .orElseThrow(() -> new ValidationException(
                        "Ciudad no encontrada: " + domain.getHomeCity()));

        UserEntity userEntity = buildUserEntity(domain, idTypeEntity, cityEntity);
        userRepository.save(userEntity);

        sendNotifications(domain, userEntity);
        
        // Métrica de éxito (solo si está disponible)
        if (userRegistrationCounter != null) {
            userRegistrationCounter.increment();
        }
        
        return Void.returnVoid();
        } catch (Exception e) {
            // Métrica de error (solo si está disponible)
            if (meterRegistry != null) {
                meterRegistry.counter("user.registrations.errors", 
                    "operation", "register",
                    "error_type", e.getClass().getSimpleName())
                    .increment();
            } else if (userRegistrationErrorCounter != null) {
                userRegistrationErrorCounter.increment();
            }
            throw e;
        } finally {
            if (sample != null && userRegistrationTimer != null) {
                sample.stop(userRegistrationTimer);
            }
        }
    }

    private void validateUserDoesNotExist(RegisterUserDomain domain) {
        if (userRepository.existsByIdNumber(domain.getIdNumber())) {
            throw new UserAlreadyExistsException(
                    "El número de identificación ya está registrado: " + domain.getIdNumber());
        }

        if (userRepository.existsByEmail(domain.getEmail())) {
            throw new UserAlreadyExistsException(
                    "El correo electrónico ya está registrado: " + domain.getEmail());
        }

        if (userRepository.existsByMobileNumber(domain.getMobileNumber())) {
            throw new UserAlreadyExistsException(
                    "El número de celular ya está registrado: " + domain.getMobileNumber());
        }
    }

    private void validateRequiredFields(RegisterUserDomain domain) {
        if (domain.getIdNumber() == null || domain.getIdNumber().isBlank()) {
            throw new ValidationException("El número de identificación es obligatorio");
        }

        if (domain.getFirstName() == null || domain.getFirstName().isBlank()) {
            throw new ValidationException("El primer nombre es obligatorio");
        }

        if (domain.getFirstSurname() == null || domain.getFirstSurname().isBlank()) {
            throw new ValidationException("El primer apellido es obligatorio");
        }

        if (domain.getEmail() == null || domain.getEmail().isBlank()) {
            throw new ValidationException("El correo electrónico es obligatorio");
        }

        if (!domain.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new ValidationException("El correo electrónico no tiene un formato válido");
        }

        if (domain.getMobileNumber() == null || domain.getMobileNumber().isBlank()) {
            throw new ValidationException("El número de celular es obligatorio");
        }

        if (!domain.getMobileNumber().matches("^\\+?[0-9]{10,15}$")) {
            throw new ValidationException("El número de celular no tiene un formato válido");
        }
    }

    private void setDefaultsIfNeeded(RegisterUserDomain domain) {
        if (domain.getIdType() == null || domain.getIdType().equals(UUIDHelper.getDefault())) {
            domain.setIdType(UUID.fromString("44444444-4444-4444-4444-444444444444"));
        }

        if (domain.getHomeCity() == null || domain.getHomeCity().equals(UUIDHelper.getDefault())) {
            domain.setHomeCity(UUID.fromString("33333333-3333-3333-3333-333333333333"));
        }
    }

    private UserEntity buildUserEntity(
            RegisterUserDomain domain,
            IdTypeEntity idTypeEntity,
            CityEntity cityEntity) {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setIdType(idTypeEntity);
        userEntity.setIdNumber(domain.getIdNumber());
        userEntity.setFirstName(domain.getFirstName());
        userEntity.setSecondName(domain.getSecondName());
        userEntity.setFirstSurname(domain.getFirstSurname());
        userEntity.setSecondSurname(domain.getSecondSurname());
        userEntity.setHomeCity(cityEntity);
        userEntity.setEmail(domain.getEmail());
        userEntity.setMobileNumber(domain.getMobileNumber());
        userEntity.setEmailConfirmed(false);
        userEntity.setMobileNumberConfirmed(false);

        // Generar códigos de confirmación (ambos de 6 dígitos)
        String emailCode = generateMobileConfirmationCode();
        String mobileCode = generateMobileConfirmationCode();
        
        userEntity.setEmailConfirmationToken(emailCode);
        userEntity.setMobileConfirmationToken(mobileCode);
        userEntity.setEmailConfirmationTokenExpiry(LocalDateTime.now().plusDays(1));
        userEntity.setMobileConfirmationTokenExpiry(LocalDateTime.now().plusDays(1));

        return userEntity;
    }

    private String generateMobileConfirmationCode() {
        // Generar código de 6 dígitos para confirmación (SMS o Email)
        return String.format("%06d", (int)(Math.random() * 1000000));
    }

    private void sendNotifications(RegisterUserDomain domain, UserEntity userEntity) {
        try {
            // Enviar email de bienvenida
            notificationClient.sendWelcomeEmail(domain.getEmail(), domain.getFirstName());
            
            // Enviar email de confirmación con código
            notificationClient.sendEmailConfirmation(
                domain.getEmail(), 
                domain.getFirstName(), 
                userEntity.getEmailConfirmationToken(),
                userEntity.getId().toString()
            );
            
            // Enviar SMS de bienvenida
            notificationClient.sendWelcomeSms(domain.getMobileNumber(), domain.getFirstName());
            
            // Enviar SMS de confirmación con código
            notificationClient.sendMobileConfirmation(
                domain.getMobileNumber(), 
                domain.getFirstName(), 
                userEntity.getMobileConfirmationToken()
            );
            
        } catch (Exception e) {
            System.err.println("Error enviando notificaciones: " + e.getMessage());
        }
    }
}