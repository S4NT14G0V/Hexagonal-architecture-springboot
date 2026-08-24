package com.backend.hexagonal.infrastructure.adapter.in.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import com.backend.hexagonal.application.exception.UserAlreadyExistsException;
import com.backend.hexagonal.application.exception.UserNotFoundException;
import com.backend.hexagonal.application.port.in.CreateUserUseCase;
import com.backend.hexagonal.application.port.in.DeleteUserUseCase;
import com.backend.hexagonal.application.port.in.GetByIdUserUseCase;
import com.backend.hexagonal.application.port.in.GetAllUserUseCase;
import com.backend.hexagonal.application.port.in.UpdateUserUseCase;
import com.backend.hexagonal.domain.model.User;
import com.backend.hexagonal.infrastructure.adapter.in.web.dto.UserRequestDto;

import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.context.annotation.Import;
import com.backend.hexagonal.infrastructure.adapter.in.web.mapper.UserWebMapper;

@WebMvcTest(UserController.class)
@Import(UserWebMapper.class)
public class UserControllerTest {

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private GetAllUserUseCase getAllUserUseCase;

    @MockitoBean
    private GetByIdUserUseCase getByIdUserUseCase;

    @MockitoBean
    private UpdateUserUseCase updateUserUseCase;

    @MockitoBean
    private DeleteUserUseCase deleteUserUseCase;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return 204 No Content when delete a user successfully")
    void shouldReturn204NoContentWhenDeleteAUserSuccessfully() throws Exception {

        // Arrange
        UUID userId = UUID.randomUUID();

        // Act
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());

        // Assert
        verify(deleteUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should return 404 Not Found when delete a non-existent user")
    void shouldReturn404NotFoundWhenDeleteANonExistentUser() throws Exception {

        // Arrange
        UUID userId = UUID.randomUUID();

        doThrow(new UserNotFoundException(userId))
                .when(deleteUserUseCase).execute(userId);

        // Act
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNotFound());

        // Assert
        verify(deleteUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should return 201 Created when create a user successfully")
    void shouldReturn201CreatedWhenCreateUserSuccessfully() throws Exception {
        // Arrange
        UserRequestDto userRequest = new UserRequestDto(
                "John Doe",
                "john.doe@email.com");

        UUID userId = UUID.randomUUID();

        User expectedUser = new User(
                userId,
                "John Doe",
                "john.doe@email.com");

        when(createUserUseCase.execute(any(CreateUserUseCase.CreateUserCommand.class)))
                .thenReturn(expectedUser);

        // Act
        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expectedUser.getId().toString()))
                .andExpect(jsonPath("$.name").value(expectedUser.getName()))
                .andExpect(jsonPath("$.email").value(expectedUser.getEmail()));

        // Assert
        verify(createUserUseCase)
                .execute(any(CreateUserUseCase.CreateUserCommand.class));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when create user with invalid email")
    void shouldReturn400BadRequestWhenCreateUserWithInvalidEmail() throws Exception {

        // Arrange
        UserRequestDto userRequest = new UserRequestDto(
                "John Doe",
                "invalid-email");

        // Act
        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());

        // Assert
        verify(createUserUseCase, never())
                .execute(any(CreateUserUseCase.CreateUserCommand.class));
    }

    @Test
    @DisplayName("Should return 409 Conflict when user already exists")
    void shouldReturn409ConflictWhenUserAlreadyExists() throws Exception {

        // Arrange
        UserRequestDto userRequest = new UserRequestDto(
                "John Doe",
                "john.doe@email.com");

        when(createUserUseCase.execute(any(CreateUserUseCase.CreateUserCommand.class)))
                .thenThrow(new UserAlreadyExistsException(userRequest.email()));

        // Act
        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isConflict());

        // Assert
        verify(createUserUseCase)
                .execute(any(CreateUserUseCase.CreateUserCommand.class));
    }

    @Test
    @DisplayName("Should return 200 Ok when get all users successfully")
    void shouldReturn200OkWhenGetAllUsersSuccessfully() throws Exception {
        // Arrange
        List<User> expectedUsers = List.of(
                new User(UUID.randomUUID(), "John Doe", "john.doe@email.com"),
                new User(UUID.randomUUID(), "Jane Doe", "jane.doe@email.com"));

        when(getAllUserUseCase.execute())
                .thenReturn(expectedUsers);

        // Act
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(expectedUsers.size()))
                .andExpect(jsonPath("$.[0].id").value(expectedUsers.get(0).getId().toString()))
                .andExpect(jsonPath("$.[0].name").value(expectedUsers.get(0).getName()))
                .andExpect(jsonPath("$.[0].email").value(expectedUsers.get(0).getEmail()))
                .andExpect(jsonPath("$.[1].id").value(expectedUsers.get(1).getId().toString()))
                .andExpect(jsonPath("$.[1].name").value(expectedUsers.get(1).getName()))
                .andExpect(jsonPath("$.[1].email").value(expectedUsers.get(1).getEmail()));

        // Assert
        verify(getAllUserUseCase).execute();
    }

    @Test
    @DisplayName("Should return 200 Ok with empty list when no users exist")
    void shouldReturn200OkWithEmptyListWhenNoUsersExist() throws Exception {
        // Arrange
        List<User> expectedUsers = List.of();

        when(getAllUserUseCase.execute())
                .thenReturn(expectedUsers);

        // Act
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(expectedUsers.size()));

        // Assert
        verify(getAllUserUseCase).execute();
    }

    @Test
    @DisplayName("Should return 200 Ok when get user by id successfully")
    void shouldReturn200OkWhenGetUserByIdSuccessfully() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();

        User expectedUser = new User(
                userId,
                "John Doe",
                "john.doe@email.com");

        when(getByIdUserUseCase.execute(userId))
                .thenReturn(expectedUser);

        // Act
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedUser.getId().toString()))
                .andExpect(jsonPath("$.name").value(expectedUser.getName()))
                .andExpect(jsonPath("$.email").value(expectedUser.getEmail()));

        // Assert
        verify(getByIdUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should return 404 Not Found when get user by id and user not found")
    void shouldReturn404NotFoundWhenGetUserByIdAndUserNotFound() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();

        when(getByIdUserUseCase.execute(userId))
                .thenThrow(new UserNotFoundException(userId));

        // Act
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound());

        // Assert
        verify(getByIdUserUseCase).execute(userId);
    }

    @Test
    @DisplayName("Should return 200 Ok when update user successfully")
    void shouldReturn200OkWhenUpdateUserSuccessfully() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();

        UserRequestDto userRequest = new UserRequestDto(
                "John Doe",
                "john.doe@email.com");

        User expectedUser = new User(
                userId,
                "John Doe",
                "john.doe@email.com");

        when(updateUserUseCase.execute(
                eq(userId),
                any(UpdateUserUseCase.UpdateUserCommand.class)))
                .thenReturn(expectedUser);

        // Act
        mockMvc.perform(put("/users/{id}", userId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedUser.getId().toString()))
                .andExpect(jsonPath("$.name").value(expectedUser.getName()))
                .andExpect(jsonPath("$.email").value(expectedUser.getEmail()));

        // Assert
        verify(updateUserUseCase)
                .execute(eq(userId), any(UpdateUserUseCase.UpdateUserCommand.class));
    }

    @Test
    @DisplayName("Should return 404 Not Found when update user and user not found")
    void shouldReturn404NotFoundWhenUpdateUserAndUserNotFound() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();

        UserRequestDto userRequest = new UserRequestDto(
                "John Doe",
                "john.doe@email.com");

        when(updateUserUseCase.execute(eq(userId), any(UpdateUserUseCase.UpdateUserCommand.class)))
                .thenThrow(new UserNotFoundException(userId));

        // Act
        mockMvc.perform(put("/users/{id}", userId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isNotFound());

        // Assert
        verify(updateUserUseCase)
                .execute(eq(userId), any(UpdateUserUseCase.UpdateUserCommand.class));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when update user with invalid email")
    void shouldReturn400BadRequestWhenUpdateUserWithInvalidEmail() throws Exception {

        // Arrange
        UUID userId = UUID.randomUUID();

        UserRequestDto userRequest = new UserRequestDto(
                "John Doe",
                "invalid-email");

        // Act
        mockMvc.perform(put("/users/{id}", userId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());

        // Assert
        verify(updateUserUseCase, never())
                .execute(
                        eq(userId),
                        any(UpdateUserUseCase.UpdateUserCommand.class));
    }

    @Test
    @DisplayName("Should create the correct command from user request")
    void shouldCreateCorrectCommandFromUserRequest() throws Exception {

        // Arrange
        UserRequestDto userRequest = new UserRequestDto(
                "John Doe",
                "john.doe@email.com");

        UUID userId = UUID.randomUUID();

        User expectedUser = new User(
                userId,
                "John Doe",
                "john.doe@email.com");

        when(createUserUseCase.execute(
                any(CreateUserUseCase.CreateUserCommand.class)))
                .thenReturn(expectedUser);

        ArgumentCaptor<CreateUserUseCase.CreateUserCommand> commandCaptor = ArgumentCaptor.forClass(
                CreateUserUseCase.CreateUserCommand.class);

        // Act
        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());

        // Assert
        verify(createUserUseCase)
                .execute(commandCaptor.capture());

        CreateUserUseCase.CreateUserCommand command = commandCaptor.getValue();

        assertEquals("John Doe", command.name());
        assertEquals("john.doe@email.com", command.email());
    }
}
