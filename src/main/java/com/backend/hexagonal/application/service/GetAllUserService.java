package com.backend.hexagonal.application.service;

import com.backend.hexagonal.application.port.in.GetAllUserUseCase;
import com.backend.hexagonal.application.port.out.UserRepositoryPort;
import com.backend.hexagonal.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class GetAllUserService implements GetAllUserUseCase {

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    @Override
    public List<User> execute() {
        return userRepositoryPort.findAll();
    }
}
