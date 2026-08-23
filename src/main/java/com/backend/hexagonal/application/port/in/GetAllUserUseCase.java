package com.backend.hexagonal.application.port.in;

import com.backend.hexagonal.domain.model.User;
import java.util.List;

public interface GetAllUserUseCase {

    List<User> execute();
}
