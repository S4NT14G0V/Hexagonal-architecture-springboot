package com.backend.hexagonal.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.hexagonal.application.exception.UserNotFoundException;
import com.backend.hexagonal.application.port.out.UserPersistencePort;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.backend.hexagonal.domain.model.User;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class DeleteUserServiceTest {

    @Mock
    private UserPersistencePort userRepositoryPort;

    @InjectMocks
    private DeleteUserService deleteUserService;

    @Test
    @DisplayName("Should delete user when user exists")
    void shouldDeleteUserWhenUserExists() {
        // Arrange
        User userToDelete = new User("John Doe", "john.doe@email.com");
        UUID userId = userToDelete.getId();

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(userToDelete));

        doNothing().when(userRepositoryPort).deleteById(userId);

        // Act
        deleteUserService.execute(userId);

        // Assert
        verify(userRepositoryPort, times(1)).findById(userId);
        verify(userRepositoryPort, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Should throw exception when user does not exist")
    void shouldThrowExceptionWhenUserDoesNotExist() {
        // Arrange
        UUID userId = UUID.randomUUID();

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            deleteUserService.execute(userId);
        });

        verify(userRepositoryPort, times(1)).findById(userId);
        verify(userRepositoryPort, never()).deleteById(any());
    }
}
