package com.backend.hexagonal.infrastructure.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.backend.hexagonal.domain.model.User;

@DataJpaTest
@Import(UserPersistenceAdapter.class)
class UserPersistenceAdapterTest {

    @Autowired
    private UserPersistenceAdapter userPersistenceAdapter;

    @Test
    @DisplayName("Should save user")
    void shouldSaveUser() {

        // Arrange
        User userToSave = new User(
                "John Doe",
                "john.doe@email.com");

        // Act
        User savedUser = userPersistenceAdapter.save(userToSave);

        // Assert
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(userToSave.getId());
        assertThat(savedUser.getName()).isEqualTo(userToSave.getName());
        assertThat(savedUser.getEmail()).isEqualTo(userToSave.getEmail());
    }

    @Test
    @DisplayName("Should find user by id")
    void shouldFindUserById() {

        // Arrange
        User userToSave = new User(
                "John Doe",
                "john.doe@email.com");

        User savedUser = userPersistenceAdapter.save(userToSave);

        // Act
        User result = userPersistenceAdapter.findById(savedUser.getId());

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(savedUser.getId());
        assertThat(result.getName()).isEqualTo(savedUser.getName());
        assertThat(result.getEmail()).isEqualTo(savedUser.getEmail());
    }

    @Test
    @DisplayName("Should return null when user does not exist")
    void shouldReturnNullWhenUserDoesNotExist() {

        // Arrange
        User user = new User(
                "John Doe",
                "john.doe@email.com");

        // Act
        User result = userPersistenceAdapter.findById(user.getId());

        // Assert
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should find all users")
    void shouldFindAllUsers() {

        // Arrange
        User firstUser = new User(
                "John Doe",
                "john.doe@email.com");

        User secondUser = new User(
                "Jane Doe",
                "jane.doe@email.com");

        userPersistenceAdapter.save(firstUser);
        userPersistenceAdapter.save(secondUser);

        // Act
        List<User> users = userPersistenceAdapter.findAll();

        // Assert
        assertThat(users)
                .hasSize(2)
                .extracting(User::getEmail)
                .containsExactlyInAnyOrder(
                        firstUser.getEmail(),
                        secondUser.getEmail());
    }

    @Test
    @DisplayName("Should delete user")
    void shouldDeleteUser() {

        // Arrange
        User userToSave = new User(
                "John Doe",
                "john.doe@email.com");

        User savedUser = userPersistenceAdapter.save(userToSave);

        // Act
        userPersistenceAdapter.deleteById(savedUser.getId());

        // Assert
        User result = userPersistenceAdapter.findById(savedUser.getId());

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should update user")
    void shouldUpdateUser() {

        // Arrange
        User userToSave = new User(
                "John Doe",
                "john.doe@email.com");

        User savedUser = userPersistenceAdapter.save(userToSave);

        User updatedUser = new User(
                "John Updated",
                "john.updated@email.com");

        // Act
        User result = userPersistenceAdapter.update(
                savedUser.getId(),
                updatedUser);

        // Assert
        assertThat(result).isNotNull();

        // The ID of the existing user must be preserved
        assertThat(result.getId()).isEqualTo(savedUser.getId());

        // The data must be updated
        assertThat(result.getName()).isEqualTo(updatedUser.getName());
        assertThat(result.getEmail()).isEqualTo(updatedUser.getEmail());

        // Verify that the update was actually persisted
        User persistedUser = userPersistenceAdapter.findById(savedUser.getId());

        assertThat(persistedUser).isNotNull();
        assertThat(persistedUser.getId()).isEqualTo(savedUser.getId());
        assertThat(persistedUser.getName()).isEqualTo(updatedUser.getName());
        assertThat(persistedUser.getEmail()).isEqualTo(updatedUser.getEmail());
    }

    @Test
    @DisplayName("Should return true when user exists by email")
    void shouldReturnTrueWhenUserExistsByEmail() {

        // Arrange
        User userToSave = new User(
                "John Doe",
                "john.doe@email.com");

        userPersistenceAdapter.save(userToSave);

        // Act
        boolean exists = userPersistenceAdapter.existsByEmail(
                userToSave.getEmail());

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when user does not exist by email")
    void shouldReturnFalseWhenUserDoesNotExistByEmail() {

        // Arrange
        String email = "john.doe@email.com";

        // Act
        boolean exists = userPersistenceAdapter.existsByEmail(email);

        // Assert
        assertThat(exists).isFalse();
    }
}