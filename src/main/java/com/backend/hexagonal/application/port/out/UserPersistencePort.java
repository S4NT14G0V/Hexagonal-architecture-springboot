package com.backend.hexagonal.application.port.out;

import com.backend.hexagonal.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPersistencePort {

    User save(User user);

    User update(UUID id, User user);

    Optional<User> findById(UUID id);

    List<User> findAll();

    void deleteById(UUID id);

    boolean existsByEmail(String email);
}
