package co.edu.uco.ucochallenge.secondary.adapters.assembler;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.UserOutputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

import java.util.UUID;

/**
 * Assembler para conversiones relacionadas con User.
 * Alternativa a los factory methods en los DTOs.
 */
public final class UserAssembler {

    private UserAssembler() {
    }

    /**
     * Convierte un Domain a Entity.
     */
    public static UserEntity toEntity(
            RegisterUserDomain domain,
            IdTypeEntity idTypeEntity,
            CityEntity cityEntity) {

        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());
        entity.setIdType(idTypeEntity);
        entity.setIdNumber(domain.getIdNumber());
        entity.setFirstName(domain.getFirstName());
        entity.setSecondName(domain.getSecondName());
        entity.setFirstSurname(domain.getFirstSurname());
        entity.setSecondSurname(domain.getSecondSurname());
        entity.setHomeCity(cityEntity);
        entity.setEmail(domain.getEmail());
        entity.setMobileNumber(domain.getMobileNumber());
        entity.setEmailConfirmed(false);
        entity.setMobileNumberConfirmed(false);

        return entity;
    }

    /**
     * Convierte Entity a OutputDTO.
     */
    public static UserOutputDTO toOutputDTO(UserEntity entity) {
        return UserOutputDTO.from(entity);
    }
}
