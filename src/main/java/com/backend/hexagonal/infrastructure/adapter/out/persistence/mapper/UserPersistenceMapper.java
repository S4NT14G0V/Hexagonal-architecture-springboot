package com.backend.hexagonal.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import com.backend.hexagonal.domain.model.User;
import com.backend.hexagonal.infrastructure.adapter.out.persistence.entity.UserJpaEntity;

@Component
public class UserPersistenceMapper {

    public UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail());
    }
}