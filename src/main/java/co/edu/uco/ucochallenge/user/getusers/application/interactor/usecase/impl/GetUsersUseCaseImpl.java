package co.edu.uco.ucochallenge.user.getusers.application.interactor.usecase.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.user.getusers.application.interactor.usecase.GetUsersUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.UserOutputDTO;
import co.edu.uco.ucochallenge.secondary.ports.repository.UserRepository;

@Service
public class GetUsersUseCaseImpl implements GetUsersUseCase {

    private final UserRepository repository;

    public GetUsersUseCaseImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserOutputDTO> execute(Void domain) {
        return repository.findAll()
            .stream()
            .map(UserOutputDTO::from)
            .collect(Collectors.toList());
    }
}
