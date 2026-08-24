package com.backend.hexagonal.application.port.in;

import com.backend.hexagonal.domain.model.User;

public interface CreateUserUseCase {

    User execute(CreateUserCommand command);

    record CreateUserCommand(String name, String email) {}
}
