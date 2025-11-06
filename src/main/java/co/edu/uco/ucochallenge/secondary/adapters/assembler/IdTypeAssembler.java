package co.edu.uco.ucochallenge.secondary.adapters.assembler;

import co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.dto.IdTypeOutputDTO;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;

/**
 * Assembler para conversiones relacionadas con IdType.
 */
public final class IdTypeAssembler {

    private IdTypeAssembler() {
    }

    public static IdTypeOutputDTO toOutputDTO(IdTypeEntity entity) {
        return IdTypeOutputDTO.from(entity);
    }
}