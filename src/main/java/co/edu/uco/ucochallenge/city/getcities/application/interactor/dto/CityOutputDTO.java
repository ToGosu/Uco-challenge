package co.edu.uco.ucochallenge.city.getcities.application.interactor.dto;

import java.util.UUID;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;

/**
 * DTO para devolver información de ciudades.
 * Requerido para GET /api/cities y referencias en otros DTOs.
 */
public record CityOutputDTO(
    UUID id,
    String name
) {
    
    /**
     * Versión simplificada sin el estado/departamento completo.
     * Útil cuando no se necesita información jerárquica.
     */
    public static CityOutputDTO fromSimple(CityEntity city) {
        if (city == null) {
            return null;
        }
        
        return new CityOutputDTO(
            city.getId(),
            city.getName()
        );
    }
}
