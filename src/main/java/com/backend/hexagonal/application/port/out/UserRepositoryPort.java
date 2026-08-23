package com.backend.hexagonal.application.port.out;

import com.backend.hexagonal.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    
    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    void deleteById(Long id);

    boolean existsByEmail(String email);
}
