package com.backend.hexagonal.application.port.in;

import java.util.UUID;

import com.backend.hexagonal.domain.model.User;

public interface UpdateUserUseCase {
    User execute(UUID id, UpdateUserCommand command);

    record UpdateUserCommand(String name, String email) {}
}
