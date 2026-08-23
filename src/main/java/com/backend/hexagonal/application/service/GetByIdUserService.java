package com.backend.hexagonal.application.service;

import com.backend.hexagonal.application.exception.UserNotFoundException;
import com.backend.hexagonal.application.port.in.GetByIdUserUseCase;
import com.backend.hexagonal.application.port.out.UserRepositoryPort;
import com.backend.hexagonal.domain.model.User;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

public class GetByIdUserService implements GetByIdUserUseCase {

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    @Override
    public User execute(UUID id) {
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
