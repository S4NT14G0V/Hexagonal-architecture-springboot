package com.backend.hexagonal.infrastructure.adapter.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.backend.hexagonal.application.port.out.UserPersistencePort;
import com.backend.hexagonal.domain.model.User;
import com.backend.hexagonal.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.backend.hexagonal.infrastructure.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.backend.hexagonal.infrastructure.adapter.out.persistence.repositoy.SpringDataUserRepository;

@Component
public class UserPersistenceAdapter implements UserPersistencePort {

    private final SpringDataUserRepository repository;

    private final UserPersistenceMapper mapper;

    public UserPersistenceAdapter(SpringDataUserRepository repository, UserPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toEntity(user);
        UserJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public User update(UUID id, User user) {
        return repository.findById(id)
                .map(entity -> {
                    entity.setName(user.getName());
                    entity.setEmail(user.getEmail());
                    return mapper.toDomain(repository.save(entity));
                })
                .orElse(null);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
