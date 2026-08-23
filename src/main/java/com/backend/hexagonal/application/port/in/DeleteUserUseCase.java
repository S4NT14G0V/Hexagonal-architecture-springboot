package com.backend.hexagonal.application.port.in;

import java.util.UUID;

public interface DeleteUserUseCase {
    void execute(UUID id);
}
