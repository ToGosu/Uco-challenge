package co.edu.uco.ucochallenge.city.getcities.application.interactor.usecase.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.city.getcities.application.interactor.dto.CityOutputDTO;
import co.edu.uco.ucochallenge.city.getcities.application.interactor.usecase.GetCitiesUseCase;
import co.edu.uco.ucochallenge.secondary.ports.repository.CityRepository;

@Service
public class GetCitiesUseCaseImpl implements GetCitiesUseCase {

    private final CityRepository repository;

    public GetCitiesUseCaseImpl(CityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CityOutputDTO> execute(Void domain) {
        return repository.findAll()
            .stream()
            .map(CityOutputDTO::fromSimple)
            .collect(Collectors.toList());
    }
}
