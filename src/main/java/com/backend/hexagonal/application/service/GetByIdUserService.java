package com.backend.hexagonal.application.service;

import java.util.UUID;

import com.backend.hexagonal.application.exception.UserNotFoundException;
import com.backend.hexagonal.application.port.in.GetByIdUserUseCase;
import com.backend.hexagonal.application.port.out.UserPersistencePort;
import com.backend.hexagonal.domain.model.User;

public class GetByIdUserService implements GetByIdUserUseCase {

    private final UserPersistencePort userRepositoryPort;

    public GetByIdUserService(UserPersistencePort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User execute(UUID id) {
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
