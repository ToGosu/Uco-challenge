package co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.usecase.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.dto.IdTypeOutputDTO;
import co.edu.uco.ucochallenge.idtype.getidtypes.application.interactor.usecase.GetIdTypesUseCase;
import co.edu.uco.ucochallenge.secondary.ports.repository.IdTypeRepository;

@Service
public class GetIdTypesUseCaseImpl implements GetIdTypesUseCase {

    private final IdTypeRepository repository;

    public GetIdTypesUseCaseImpl(IdTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<IdTypeOutputDTO> execute(Void domain) {
        return repository.findAll()
            .stream()
            .map(IdTypeOutputDTO::from)
            .collect(Collectors.toList());
    }
}
