package com.backend.hexagonal.infrastructure.adapter.in.web.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hexagonal.application.port.in.CreateUserUseCase;
import com.backend.hexagonal.application.port.in.DeleteUserUseCase;
import com.backend.hexagonal.application.port.in.GetAllUserUseCase;
import com.backend.hexagonal.application.port.in.GetByIdUserUseCase;
import com.backend.hexagonal.application.port.in.UpdateUserUseCase;
import com.backend.hexagonal.domain.model.User;
import com.backend.hexagonal.infrastructure.adapter.in.web.dto.UserRequestDto;
import com.backend.hexagonal.infrastructure.adapter.in.web.dto.UserResponseDto;
import com.backend.hexagonal.infrastructure.adapter.in.web.mapper.UserWebMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetAllUserUseCase getAllUserUseCase;
    private final GetByIdUserUseCase getByIdUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UserWebMapper userWebMapper;

    public UserController(
            CreateUserUseCase createUserUseCase,
            GetAllUserUseCase getAllUserUseCase,
            GetByIdUserUseCase getByIdUserUseCase,
            UpdateUserUseCase updateUserUseCase,
            DeleteUserUseCase deleteUserUseCase,
            UserWebMapper userWebMapper) {
        this.createUserUseCase = createUserUseCase;
        this.getAllUserUseCase = getAllUserUseCase;
        this.getByIdUserUseCase = getByIdUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.userWebMapper = userWebMapper;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto request) {
        User user = createUserUseCase.execute(userWebMapper.toCreateCommand(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userWebMapper.toResponse(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<User> users = getAllUserUseCase.execute();
        List<UserResponseDto> response = users.stream()
                .map(userWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable UUID id) {
        User user = getByIdUserUseCase.execute(id);
        return ResponseEntity.ok(userWebMapper.toResponse(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserRequestDto request) {
        User user = updateUserUseCase.execute(id, userWebMapper.toUpdateCommand(request));
        return ResponseEntity.ok(userWebMapper.toResponse(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
