package com.backend.hexagonal.infrastructure.adapter.in.web.dto;

import com.backend.hexagonal.domain.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequestDto(
        @NotBlank(message = "User name cannot be null or empty")
        String name,

        @NotBlank(message = "User email cannot be null or empty")
        @Pattern(regexp = User.EMAIL_REGEX, message = "User email format is invalid")
        String email
) {
}
