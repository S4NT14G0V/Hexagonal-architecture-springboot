package com.backend.hexagonal.application.service;

import com.backend.hexagonal.application.exception.UserAlreadyExistsException;
import com.backend.hexagonal.application.port.in.CreateUserUseCase;
import com.backend.hexagonal.application.port.out.UserPersistencePort;
import com.backend.hexagonal.domain.model.User;

public class CreateUserService implements CreateUserUseCase {

    private final UserPersistencePort userRepositoryPort;

    public CreateUserService(UserPersistencePort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User execute(CreateUserCommand command) {
        User user = new User(command.name(), command.email());

        if (userRepositoryPort.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail());
        }

        return userRepositoryPort.save(user);
    }
}
