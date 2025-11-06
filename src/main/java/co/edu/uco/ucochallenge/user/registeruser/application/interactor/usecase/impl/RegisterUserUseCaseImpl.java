package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.secondary.adapters.client.NotificationClient;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.secondary.ports.repository.UserRepository;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.RegisterUserUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;

@Service
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository repository;
    private final NotificationClient notificationClient;

    public RegisterUserUseCaseImpl(UserRepository repository, NotificationClient notificationClient) {
        this.repository = repository;
        this.notificationClient = notificationClient;
    }
    

    @Override
    public Void execute(final RegisterUserDomain domain) {

        if (repository.existsByIdNumber(domain.getIdNumber())) {
            throw new IllegalArgumentException("El número de identificación ya está registrado.");
        }

        if (repository.existsByEmail(domain.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        if (repository.existsByMobileNumber(domain.getMobileNumber())) {
            throw new IllegalArgumentException("El número de celular ya está registrado.");
        }
        
        if (domain.getIdType() == null || domain.getIdType().equals(UUIDHelper.getDefault())) {
            domain.setIdType(UUID.fromString("44444444-4444-4444-4444-444444444444"));
        }
        
        if (domain.getHomeCity() == null || domain.getHomeCity().equals(UUIDHelper.getDefault())) {
            domain.setHomeCity(UUID.fromString("33333333-3333-3333-3333-333333333333"));
        }
    	
        // 🔹 Mapeo Domain → Entity
        var idTypeEntity = new IdTypeEntity();
        idTypeEntity.setId(domain.getIdType());
        
        var cityEntity = new CityEntity();
        cityEntity.setId(domain.getHomeCity());
        
        

        var userEntity = new UserEntity();
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
        
        


        repository.save(userEntity);

        // 🔹 Notificaciones externas
        try {
            notificationClient.sendWelcomeEmail(domain.getEmail(), domain.getFirstName());
            notificationClient.sendWelcomeSms(domain.getMobileNumber(), domain.getFirstName());
        } catch (Exception e) {
            System.out.println("⚠️ Error enviando notificaciones: " + e.getMessage());
        }

        return null;
    }
}
