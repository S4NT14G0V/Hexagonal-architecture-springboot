package com.backend.hexagonal.infrastructure.adapter.in.web.dto;

import java.util.UUID;
import com.backend.hexagonal.domain.model.User;

public record UserResponseDto(
        UUID id,
        String name,
        String email
) {
    public static UserResponseDto from(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }
}
