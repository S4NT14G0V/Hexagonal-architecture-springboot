package com.backend.hexagonal.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.backend.hexagonal.application.port.out.UserPersistencePort;

import com.backend.hexagonal.domain.model.User;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetAllUserService")
class GetAllUserServiceTest {

    @Mock
    private UserPersistencePort userRepositoryPort;

    @InjectMocks
    private GetAllUserService getAllUserService;

    @Test
    @DisplayName("Should return all users from repository")
    void shouldReturnAllUsers_whenExists() {

        // Arrange
        User userOne = new User("John Doe", "john.doe@email.com");
        User userTwo = new User("Jane Doe", "jane.doe@email.com");
        
        List<User> users = List.of(userOne, userTwo);

        when(userRepositoryPort.findAll()).thenReturn(users);

        // Act
        List<User> result = getAllUserService.execute();

        // Assert
        assertThat(result).containsExactlyInAnyOrder(userOne, userTwo);

        verify(userRepositoryPort).findAll();
        verifyNoMoreInteractions(userRepositoryPort);
    }

    @Test
    @DisplayName("Should return empty list when no users exist")
    void shouldReturnEmptyList_whenNoUsersExist() {

        // Arrange
        when(userRepositoryPort.findAll()).thenReturn(List.of());
        
        // Act
        List<User> result = getAllUserService.execute();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(userRepositoryPort).findAll();
        verifyNoMoreInteractions(userRepositoryPort);
    }
}
