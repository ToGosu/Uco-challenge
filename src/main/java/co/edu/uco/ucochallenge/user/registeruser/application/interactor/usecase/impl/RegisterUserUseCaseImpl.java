package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.impl;

import java.time.LocalDateTime;
import java.util.UUID;

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

@Service
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final IdTypeRepository idTypeRepository;
    private final CityRepository cityRepository;
    private final NotificationClient notificationClient;

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

    @Override
    public Void execute(final RegisterUserDomain domain) {

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

        return Void.returnVoid();
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

        // Generar tokens de confirmación
        String emailToken = UUID.randomUUID().toString();
        String mobileToken = generateMobileConfirmationCode();
        
        userEntity.setEmailConfirmationToken(emailToken);
        userEntity.setMobileConfirmationToken(mobileToken);
        userEntity.setEmailConfirmationTokenExpiry(LocalDateTime.now().plusDays(1));
        userEntity.setMobileConfirmationTokenExpiry(LocalDateTime.now().plusDays(1));

        return userEntity;
    }

    private String generateMobileConfirmationCode() {
        // Generar código de 6 dígitos para SMS
        return String.format("%06d", (int)(Math.random() * 1000000));
    }

    private void sendNotifications(RegisterUserDomain domain, UserEntity userEntity) {
        try {
            // Enviar email de bienvenida
            notificationClient.sendWelcomeEmail(domain.getEmail(), domain.getFirstName());
            
            // Enviar email de confirmación con token
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