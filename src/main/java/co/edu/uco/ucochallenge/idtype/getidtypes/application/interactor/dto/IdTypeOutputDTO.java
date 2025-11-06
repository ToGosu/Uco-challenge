package co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.dto;

import java.util.UUID;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;

/**
 * DTO para devolver tipos de identificación.
 * Requerido para GET /api/idtypes.
 */
public record IdTypeOutputDTO(
    UUID id,
    String name
) {
    
    /**
     * Crea un IdTypeOutputDTO a partir de una entidad IdType.
     */
    public static IdTypeOutputDTO from(IdTypeEntity idType) {
        if (idType == null) {
            return null;
        }
        
        return new IdTypeOutputDTO(
            idType.getId(),
            idType.getName()
        );
    }
    
    /**
     * Versión simplificada solo con datos básicos.
     * Útil para combos/selects en el frontend.
     */
    public static IdTypeOutputDTO fromSimple(IdTypeEntity idType) {
        if (idType == null) {
            return null;
        }
        
        return new IdTypeOutputDTO(
            idType.getId(),
            idType.getName()
        );
    }
}
