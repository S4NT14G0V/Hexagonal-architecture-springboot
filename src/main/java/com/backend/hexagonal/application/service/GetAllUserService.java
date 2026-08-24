package com.backend.hexagonal.application.service;

import java.util.List;

import com.backend.hexagonal.application.port.in.GetAllUserUseCase;
import com.backend.hexagonal.application.port.out.UserPersistencePort;
import com.backend.hexagonal.domain.model.User;

public class GetAllUserService implements GetAllUserUseCase {

    private final UserPersistencePort userRepositoryPort;

    public GetAllUserService(UserPersistencePort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public List<User> execute() {
        return userRepositoryPort.findAll();
    }
}
