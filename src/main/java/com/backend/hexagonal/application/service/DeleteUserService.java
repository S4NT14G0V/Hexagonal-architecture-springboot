package com.backend.hexagonal.application.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.backend.hexagonal.application.exception.UserNotFoundException;
import com.backend.hexagonal.application.port.in.DeleteUserUseCase;
import com.backend.hexagonal.application.port.out.UserPersistencePort;

public class DeleteUserService implements DeleteUserUseCase {

    @Autowired
    private UserPersistencePort userRepositoryPort;

    @Override
    public void execute(UUID id) {
        userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepositoryPort.deleteById(id);
    }
}
