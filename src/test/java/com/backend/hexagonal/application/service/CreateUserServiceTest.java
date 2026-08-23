package com.backend.hexagonal.application.service;

import com.backend.hexagonal.application.exception.UserAlreadyExistsException;
import com.backend.hexagonal.application.port.in.CreateUserUseCase;
import com.backend.hexagonal.application.port.out.UserRepositoryPort;
import com.backend.hexagonal.domain.exception.InvalidUserDataException;
import com.backend.hexagonal.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateUserService")
class CreateUserServiceTest {

        @Mock
        private UserRepositoryPort userRepositoryPort;

        @InjectMocks
        private CreateUserService createUserService;

        private CreateUserUseCase.CreateUserCommand validCommand;

        @BeforeEach
        void setUp() {
                validCommand = new CreateUserUseCase.CreateUserCommand(
                                "John Doe",
                                "john.doe@email.com");
        }

        @Nested
        @DisplayName("Successful Creation")
        class SuccessTests {

                @Test
                @DisplayName("Should create and return user when email is unique")
                void shouldCreateAndReturnUser_whenEmailIsUnique() {
                        // Arrange
                        User savedUser = new User(
                                        "John Doe",
                                        "john.doe@email.com");

                        when(userRepositoryPort.existsByEmail(validCommand.email()))
                                        .thenReturn(false);

                        when(userRepositoryPort.save(any(User.class)))
                                        .thenReturn(savedUser);

                        // Act
                        User createdUser = createUserService.execute(validCommand);

                        // Assert
                        assertThat(createdUser).isNotNull();
                        assertThat(createdUser.getId()).isEqualTo(savedUser.getId());
                        assertThat(createdUser.getName()).isEqualTo(savedUser.getName());
                        assertThat(createdUser.getEmail()).isEqualTo(savedUser.getEmail());

                        verify(userRepositoryPort)
                                        .existsByEmail(validCommand.email());

                        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

                        verify(userRepositoryPort)
                                        .save(userCaptor.capture());

                        User userToSave = userCaptor.getValue();

                        assertThat(userToSave.getName())
                                        .isEqualTo(validCommand.name());

                        assertThat(userToSave.getEmail())
                                        .isEqualTo(validCommand.email());
                }
        }

        @Nested
        @DisplayName("Validation Errors")
        class ValidationTests {

                @Test
                @DisplayName("Should throw InvalidUserDataException when name is null")
                void shouldThrowInvalidUserDataException_whenNameIsNull() {
                        // Arrange
                        CreateUserUseCase.CreateUserCommand command = new CreateUserUseCase.CreateUserCommand(
                                        null,
                                        "john.doe@email.com");

                        // Act & Assert
                        assertThatThrownBy(() -> createUserService.execute(command))
                                        .isInstanceOf(InvalidUserDataException.class)
                                        .hasMessage("User name cannot be null or empty");

                        verify(userRepositoryPort, never())
                                        .existsByEmail(any());

                        verify(userRepositoryPort, never())
                                        .save(any());
                }

                @Test
                @DisplayName("Should throw InvalidUserDataException when name is empty")
                void shouldThrowInvalidUserDataException_whenNameIsEmpty() {
                        // Arrange
                        CreateUserUseCase.CreateUserCommand command = new CreateUserUseCase.CreateUserCommand(
                                        "",
                                        "john.doe@email.com");

                        // Act & Assert
                        assertThatThrownBy(() -> createUserService.execute(command))
                                        .isInstanceOf(InvalidUserDataException.class)
                                        .hasMessage("User name cannot be null or empty");

                        verify(userRepositoryPort, never())
                                        .existsByEmail(any());

                        verify(userRepositoryPort, never())
                                        .save(any());
                }

                @Test
                @DisplayName("Should throw InvalidUserDataException when name is blank")
                void shouldThrowInvalidUserDataException_whenNameIsBlank() {
                        // Arrange
                        CreateUserUseCase.CreateUserCommand command = new CreateUserUseCase.CreateUserCommand(
                                        "   ",
                                        "john.doe@email.com");

                        // Act & Assert
                        assertThatThrownBy(() -> createUserService.execute(command))
                                        .isInstanceOf(InvalidUserDataException.class)
                                        .hasMessage("User name cannot be null or empty");

                        verify(userRepositoryPort, never())
                                        .existsByEmail(any());

                        verify(userRepositoryPort, never())
                                        .save(any());
                }

                @Test
                @DisplayName("Should throw InvalidUserDataException when email is null")
                void shouldThrowInvalidUserDataException_whenEmailIsNull() {
                        // Arrange
                        CreateUserUseCase.CreateUserCommand command = new CreateUserUseCase.CreateUserCommand(
                                        "John Doe",
                                        null);

                        // Act & Assert
                        assertThatThrownBy(() -> createUserService.execute(command))
                                        .isInstanceOf(InvalidUserDataException.class)
                                        .hasMessage("User email cannot be null or empty");

                        verify(userRepositoryPort, never())
                                        .existsByEmail(any());

                        verify(userRepositoryPort, never())
                                        .save(any());
                }

                @Test
                @DisplayName("Should throw InvalidUserDataException when email is empty")
                void shouldThrowInvalidUserDataException_whenEmailIsEmpty() {
                        // Arrange
                        CreateUserUseCase.CreateUserCommand command = new CreateUserUseCase.CreateUserCommand(
                                        "John Doe",
                                        "");

                        // Act & Assert
                        assertThatThrownBy(() -> createUserService.execute(command))
                                        .isInstanceOf(InvalidUserDataException.class)
                                        .hasMessage("User email cannot be null or empty");

                        verify(userRepositoryPort, never())
                                        .existsByEmail(any());

                        verify(userRepositoryPort, never())
                                        .save(any());
                }

                @Test
                @DisplayName("Should throw InvalidUserDataException when email is blank")
                void shouldThrowInvalidUserDataException_whenEmailIsBlank() {
                        // Arrange
                        CreateUserUseCase.CreateUserCommand command = new CreateUserUseCase.CreateUserCommand(
                                        "John Doe",
                                        "   ");

                        // Act & Assert
                        assertThatThrownBy(() -> createUserService.execute(command))
                                        .isInstanceOf(InvalidUserDataException.class)
                                        .hasMessage("User email cannot be null or empty");

                        verify(userRepositoryPort, never())
                                        .existsByEmail(any());

                        verify(userRepositoryPort, never())
                                        .save(any());
                }

                @Test
                @DisplayName("Should throw InvalidUserDataException when email format is invalid")
                void shouldThrowInvalidUserDataException_whenEmailFormatIsInvalid() {
                        // Arrange
                        CreateUserUseCase.CreateUserCommand command = new CreateUserUseCase.CreateUserCommand(
                                        "John Doe",
                                        "invalid-email");

                        // Act & Assert
                        assertThatThrownBy(() -> createUserService.execute(command))
                                        .isInstanceOf(InvalidUserDataException.class)
                                        .hasMessage("User email format is invalid");

                        verify(userRepositoryPort, never())
                                        .existsByEmail(any());

                        verify(userRepositoryPort, never())
                                        .save(any());
                }
        }

        @Nested
        @DisplayName("Business Rules")
        class BusinessRuleTests {

                @Test
                @DisplayName("Should throw UserAlreadyExistsException when email already exists")
                void shouldThrowUserAlreadyExistsException_whenEmailExists() {
                        // Arrange
                        when(userRepositoryPort.existsByEmail(validCommand.email()))
                                        .thenReturn(true);

                        // Act & Assert
                        assertThatThrownBy(() -> createUserService.execute(validCommand))
                                        .isInstanceOf(UserAlreadyExistsException.class)
                                        .hasMessage("User with email " + validCommand.email() + " already exists");

                        verify(userRepositoryPort)
                                        .existsByEmail(validCommand.email());

                        verify(userRepositoryPort, never())
                                        .save(any());
                }
        }
}