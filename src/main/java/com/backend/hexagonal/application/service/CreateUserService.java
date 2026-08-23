package com.backend.hexagonal.application.service;

import com.backend.hexagonal.application.port.in.CreateUserUseCase;
import com.backend.hexagonal.domain.model.User;
import com.backend.hexagonal.application.port.out.UserRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import com.backend.hexagonal.application.exception.UserAlreadyExistsException;

public class CreateUserService implements CreateUserUseCase {

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    @Override
    public User execute(CreateUserCommand command) {

        User user = new User(command.name(), command.email());

        if (userRepositoryPort.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }

        return userRepositoryPort.save(user);
    }
}
