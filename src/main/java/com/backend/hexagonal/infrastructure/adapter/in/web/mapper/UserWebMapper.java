package com.backend.hexagonal.infrastructure.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import com.backend.hexagonal.application.port.in.CreateUserUseCase.CreateUserCommand;
import com.backend.hexagonal.application.port.in.UpdateUserUseCase.UpdateUserCommand;
import com.backend.hexagonal.domain.model.User;
import com.backend.hexagonal.infrastructure.adapter.in.web.dto.UserRequestDto;
import com.backend.hexagonal.infrastructure.adapter.in.web.dto.UserResponseDto;

@Component
public class UserWebMapper {

    public CreateUserCommand toCreateCommand(UserRequestDto request) {
        return new CreateUserCommand(request.name(), request.email());
    }

    public UpdateUserCommand toUpdateCommand(UserRequestDto request) {
        return new UpdateUserCommand(request.name(), request.email());
    }

    public UserResponseDto toResponse(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }
}
