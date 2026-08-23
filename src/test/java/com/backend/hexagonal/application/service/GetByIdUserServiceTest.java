package com.backend.hexagonal.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.backend.hexagonal.application.port.out.UserRepositoryPort;

import com.backend.hexagonal.domain.model.User;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import com.backend.hexagonal.application.exception.UserNotFoundException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetByIdUserService")
class GetByIdUserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private GetByIdUserService getByIdUserService;

    @Test
    @DisplayName("Should return user when exists")
    void shouldReturnUser_whenExists() {

        // Arrange
        User user = new User("John Doe", "john.doe@email.com");
        UUID uuid = user.getId();

        when(userRepositoryPort.findById(uuid)).thenReturn(Optional.of(user));

        // Act
        User expectedUser = getByIdUserService.execute(uuid);

        // Assert
        assertThat(expectedUser).isNotNull();
        assertThat(expectedUser).isEqualTo(user);

        verify(userRepositoryPort).findById(uuid);
        verifyNoMoreInteractions(userRepositoryPort);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not exist")
    void shouldThrowUserNotFoundException_whenUserDoesNotExist() {

        // Arrange
        UUID uuid = UUID.randomUUID();

        when(userRepositoryPort.findById(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> getByIdUserService.execute(uuid))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with ID " + uuid + " not found");

        verify(userRepositoryPort).findById(uuid);
        verifyNoMoreInteractions(userRepositoryPort);
    }
}
