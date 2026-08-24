package com.backend.hexagonal.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(
        @NotBlank(message = "User name cannot be null or empty")
        String name,

        @NotBlank(message = "User email cannot be null or empty")
        @Email(message = "User email format is invalid")
        String email
) {
}
