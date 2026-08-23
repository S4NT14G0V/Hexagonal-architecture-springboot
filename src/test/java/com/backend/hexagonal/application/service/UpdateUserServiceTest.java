package com.backend.hexagonal.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.hexagonal.application.exception.UserAlreadyExistsException;
import com.backend.hexagonal.application.exception.UserNotFoundException;
import com.backend.hexagonal.application.port.in.UpdateUserUseCase;
import com.backend.hexagonal.application.port.out.UserRepositoryPort;
import com.backend.hexagonal.domain.model.User;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateUserService")
class UpdateUserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private UpdateUserService updateUserService;

    @Test
    @DisplayName("Should update user when exists")
    void shouldUpdateUser_whenExists() {

        // Arrange
        User existingUser = new User(
                "John Doe",
                "john.doe@email.com");

        UUID userId = existingUser.getId();

        UpdateUserUseCase.UpdateUserCommand command = new UpdateUserUseCase.UpdateUserCommand(
                "Jane Doe",
                "jane.doe@email.com");

        when(userRepositoryPort.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(userRepositoryPort.existsByEmail(command.email()))
                .thenReturn(false);

        when(userRepositoryPort.update(eq(userId), any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(1));

        // Act
        User result = updateUserService.execute(userId, command);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo(command.name());
        assertThat(result.getEmail()).isEqualTo(command.email());

        verify(userRepositoryPort)
                .findById(userId);

        verify(userRepositoryPort)
                .existsByEmail(command.email());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepositoryPort)
                .update(eq(userId), userCaptor.capture());

        User updatedUser = userCaptor.getValue();

        assertThat(updatedUser.getId())
                .isEqualTo(userId);

        assertThat(updatedUser.getName())
                .isEqualTo(command.name());

        assertThat(updatedUser.getEmail())
                .isEqualTo(command.email());
    }

    @Test
    @DisplayName("Should update user without checking email when email remains unchanged")
    void shouldUpdateUser_whenEmailRemainsUnchanged() {

        // Arrange
        User existingUser = new User(
                "John Doe",
                "john.doe@email.com");

        UUID userId = existingUser.getId();

        UpdateUserUseCase.UpdateUserCommand command = new UpdateUserUseCase.UpdateUserCommand(
                "Jane Doe",
                "john.doe@email.com");

        when(userRepositoryPort.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(userRepositoryPort.update(eq(userId), any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(1));

        // Act
        User result = updateUserService.execute(userId, command);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo(command.name());
        assertThat(result.getEmail()).isEqualTo(command.email());

        verify(userRepositoryPort)
                .findById(userId);

        verify(userRepositoryPort, never())
                .existsByEmail(any());

        verify(userRepositoryPort)
                .update(eq(userId), any(User.class));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not exist")
    void shouldThrowUserNotFoundException_whenUserDoesNotExist() {

        // Arrange
        UUID userId = UUID.randomUUID();

        UpdateUserUseCase.UpdateUserCommand command = new UpdateUserUseCase.UpdateUserCommand(
                "John Updated",
                "john.updated@email.com");

        when(userRepositoryPort.findById(userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(
                () -> updateUserService.execute(userId, command))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepositoryPort)
                .findById(userId);

        verify(userRepositoryPort, never())
                .existsByEmail(any());

        verify(userRepositoryPort, never())
                .update(any(), any());
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when new email already exists")
    void shouldThrowUserAlreadyExistsException_whenNewEmailAlreadyExists() {

        // Arrange
        User existingUser = new User(
                "John Doe",
                "john.doe@email.com");

        UUID userId = existingUser.getId();

        UpdateUserUseCase.UpdateUserCommand command = new UpdateUserUseCase.UpdateUserCommand(
                "John Updated",
                "another@email.com");

        when(userRepositoryPort.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(userRepositoryPort.existsByEmail(command.email()))
                .thenReturn(true);

        // Act & Assert
        assertThatThrownBy(
                () -> updateUserService.execute(userId, command))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User with email " + command.email() + " already exists");

        verify(userRepositoryPort)
                .findById(userId);

        verify(userRepositoryPort)
                .existsByEmail(command.email());

        verify(userRepositoryPort, never())
                .update(any(), any());
    }
}