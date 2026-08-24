package com.backend.hexagonal.application.service;

import java.util.UUID;

import com.backend.hexagonal.application.exception.UserAlreadyExistsException;
import com.backend.hexagonal.application.exception.UserNotFoundException;
import com.backend.hexagonal.application.port.in.UpdateUserUseCase;
import com.backend.hexagonal.application.port.out.UserPersistencePort;
import com.backend.hexagonal.domain.model.User;

public class UpdateUserService implements UpdateUserUseCase {

    private final UserPersistencePort userRepositoryPort;

    public UpdateUserService(UserPersistencePort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User execute(UUID id, UpdateUserCommand command) {
        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!user.getEmail().equals(command.email()) && userRepositoryPort.existsByEmail(command.email())) {
            throw new UserAlreadyExistsException(command.email());
        }

        user.update(command.name(), command.email());

        return userRepositoryPort.update(id, user);
    }
}
