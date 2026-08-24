package com.backend.hexagonal.application.port.in;

import com.backend.hexagonal.domain.model.User;
import java.util.UUID;

public interface GetByIdUserUseCase {
    User execute(UUID id);
}
