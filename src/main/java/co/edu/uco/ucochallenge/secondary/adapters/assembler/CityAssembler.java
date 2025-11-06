package co.edu.uco.ucochallenge.secondary.adapters.assembler;

import co.edu.uco.ucochallenge.city.getcities.application.interactor.dto.CityOutputDTO;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;

/**
 * Assembler para conversiones relacionadas con City.
 */
public final class CityAssembler {

    private CityAssembler() {
    }

    public static CityOutputDTO toOutputDTO(CityEntity entity) {
        return CityOutputDTO.fromSimple(entity);
    }
}