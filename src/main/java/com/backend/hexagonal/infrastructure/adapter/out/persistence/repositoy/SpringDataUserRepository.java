package com.backend.hexagonal.infrastructure.adapter.out.persistence.repositoy;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.hexagonal.infrastructure.adapter.out.persistence.entity.UserJpaEntity;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, UUID> {
    boolean existsByEmail(String email);
}
