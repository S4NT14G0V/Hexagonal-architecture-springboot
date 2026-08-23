package com.backend.hexagonal.application.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.backend.hexagonal.application.exception.UserAlreadyExistsException;
import com.backend.hexagonal.application.exception.UserNotFoundException;
import com.backend.hexagonal.application.port.in.UpdateUserUseCase;
import com.backend.hexagonal.application.port.out.UserRepositoryPort;
import com.backend.hexagonal.domain.model.User;
import java.util.UUID;

public class UpdateUserService implements UpdateUserUseCase {

    @Autowired
    private UserRepositoryPort userRepositoryPort;

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
