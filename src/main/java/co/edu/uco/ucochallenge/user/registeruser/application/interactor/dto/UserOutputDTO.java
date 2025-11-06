package co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto;

import java.util.UUID;

import co.edu.uco.ucochallenge.city.getcities.application.interactor.dto.CityOutputDTO;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;

/**
 * DTO para devolver información de usuarios sin exponer entidades internas.
 * Incluye solo los datos necesarios para la capa de presentación.
 */
public record UserOutputDTO(
    UUID id,
    String idType,
    String idNumber,
    String firstName,
    String secondName,
    String firstSurname,
    String secondSurname,
    String fullName,
    String email,
    String mobileNumber,
    CityOutputDTO homeCity
) {
    
    /**
     * Crea un UserOutputDTO a partir de una entidad User.
     * Este método actúa como mapper/factory.
     */
    public static UserOutputDTO from(UserEntity user) {
        if (user == null) {
            return null;
        }
        
        return new UserOutputDTO(
            user.getId(),
            user.getIdType() != null ? user.getIdType().getName() : null,
            user.getIdNumber(),
            user.getFirstName(),
            user.getSecondName(),
            user.getFirstSurname(),
            user.getSecondSurname(),
            buildFullName(user),
            user.getEmail(),
            user.getMobileNumber(),
            user.getHomeCity() != null ? 
				CityOutputDTO.fromSimple(user.getHomeCity()) : null
        );
    }
    
    /**
     * Versión simplificada sin información sensible o completa.
     * Útil para listados o referencias.
     */
    public static UserOutputDTO fromBasic(UserEntity user) {
        if (user == null) {
            return null;
        }
        
        return new UserOutputDTO(
            user.getId(),
            null,
            null,
            user.getFirstName(),
            user.getSecondName(),
            user.getFirstSurname(),
            user.getSecondSurname(),
            buildFullName(user),
            user.getEmail(),
            null,
			null
        );
    }
    
    private static String buildFullName(UserEntity user) {
        StringBuilder fullName = new StringBuilder();
        
        if (user.getFirstName() != null && !user.getFirstName().isBlank()) {
            fullName.append(user.getFirstName());
        }
        
        if (user.getSecondName() != null && !user.getSecondName().isBlank()) {
            fullName.append(" ").append(user.getSecondName());
        }
        
        if (user.getFirstSurname() != null && !user.getFirstSurname().isBlank()) {
            fullName.append(" ").append(user.getFirstSurname());
        }
        
        if (user.getSecondSurname() != null && !user.getSecondSurname().isBlank()) {
            fullName.append(" ").append(user.getSecondSurname());
        }
        
        return fullName.toString().trim();
    }
}
