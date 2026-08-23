package com.backend.hexagonal.domain.model;

import com.backend.hexagonal.domain.exception.InvalidUserDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Nested
    @DisplayName("Creation / Instantiation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should successfully create a user with all valid parameters including ID")
        void shouldCreateUser_whenAllParametersAreValid() {
            // Arrange & Act
            User user = new User(1L, "John Doe", "john@example.com");

            // Assert
            assertThat(user).isNotNull();
            assertThat(user.getId()).isEqualTo(1L);
            assertThat(user.getName()).isEqualTo("John Doe");
            assertThat(user.getEmail()).isEqualTo("john@example.com");
        }

        @Test
        @DisplayName("Should create a user without an ID")
        void shouldCreateUser_whenIdIsNull() {
            User user = new User(null, "John Doe", "john@example.com");

            assertThat(user.getId()).isNull();
            assertThat(user.getName()).isEqualTo("John Doe");
            assertThat(user.getEmail()).isEqualTo("john@example.com");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = { "   ", "\t", "\n" })
        @DisplayName("Should throw InvalidUserDataException when name is null, empty or blank")
        void shouldThrowException_whenNameIsInvalid(String invalidName) {
            assertThatThrownBy(() -> new User(1L, invalidName, "john@example.com"))
                    .isInstanceOf(InvalidUserDataException.class)
                    .hasMessage("User name cannot be null or empty");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = { "   ", "\t", "\n" })
        @DisplayName("Should throw InvalidUserDataException when email is null, empty or blank")
        void shouldThrowException_whenEmailIsBlank(String invalidEmail) {
            assertThatThrownBy(() -> new User(1L, "John Doe", invalidEmail))
                    .isInstanceOf(InvalidUserDataException.class)
                    .hasMessage("User email cannot be null or empty");
        }

        @ParameterizedTest
        @ValueSource(strings = { "invalid-email", "john@", "@example.com", "john@com", "john.doe" })
        @DisplayName("Should throw InvalidUserDataException when email format is invalid")
        void shouldThrowException_whenEmailFormatIsInvalid(String invalidEmail) {
            assertThatThrownBy(() -> new User(1L, "John Doe", invalidEmail))
                    .isInstanceOf(InvalidUserDataException.class)
                    .hasMessage("User email format is invalid");
        }
    }

    @Nested
    @DisplayName("Business Behavior: Update Info Tests")
    class UpdateInfoTests {

        @Test
        @DisplayName("Should successfully update user information when new data is valid")
        void shouldUpdateUser_whenNewDataIsValid() {
            // Arrange
            User user = new User(1L, "John Doe", "john@example.com");

            // Act
            user.updateEmail("jane@example.com");
            user.updateName("Jane Doe");

            // Assert
            assertThat(user.getName()).isEqualTo("Jane Doe");
            assertThat(user.getEmail()).isEqualTo("jane@example.com");
        }

        @Test
        @DisplayName("Should throw InvalidUserDataException when updating with invalid name")
        void shouldThrowException_whenUpdatingWithInvalidName() {
            User user = new User(1L, "John Doe", "john@example.com");

            assertThatThrownBy(() -> user.updateName(""))
                    .isInstanceOf(InvalidUserDataException.class)
                    .hasMessage("User name cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw InvalidUserDataException when updating with invalid email")
        void shouldThrowException_whenUpdatingWithInvalidEmail() {
            User user = new User(1L, "John Doe", "john@example.com");

            assertThatThrownBy(() -> user.updateEmail("invalid-email"))
                    .isInstanceOf(InvalidUserDataException.class)
                    .hasMessage("User email format is invalid");
        }

        @Test
        @DisplayName("Should keep previous name when updating with invalid name")
        void shouldKeepPreviousName_whenUpdatingWithInvalidName() {
            User user = new User(1L, "John Doe", "john@example.com");

            assertThatThrownBy(() -> user.updateName(""))
                    .isInstanceOf(InvalidUserDataException.class);

            assertThat(user.getName()).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("Should keep previous email when updating with invalid email")
        void shouldKeepPreviousEmail_whenUpdatingWithInvalidEmail() {
            User user = new User(1L, "John Doe", "john@example.com");

            assertThatThrownBy(() -> user.updateEmail("invalid-email"))
                    .isInstanceOf(InvalidUserDataException.class);

            assertThat(user.getEmail()).isEqualTo("john@example.com");
        }
    }

    @Nested
    @DisplayName("Identity & Equality Tests (DDD Entity)")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal if two users have the same ID")
        void shouldBeEqual_whenIdsMatch() {
            User user1 = new User(1L, "John", "john@example.com");
            User user2 = new User(1L, "Jane", "jane@example.com");

            assertThat(user1).isEqualTo(user2);
            assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal if two users have different IDs")
        void shouldNotBeEqual_whenIdsDiffer() {
            User user1 = new User(1L, "John", "john@example.com");
            User user2 = new User(2L, "John", "john@example.com");

            assertThat(user1).isNotEqualTo(user2);
        }

        @Test
        @DisplayName("Should not be equal if both users have no ID")
        void shouldNotBeEqual_whenBothIdsAreNull() {
            User user1 = new User(null, "John", "john@example.com");
            User user2 = new User(null, "Jane", "jane@example.com");

            assertThat(user1).isNotEqualTo(user2);
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            User user = new User(1L, "John", "john@example.com");

            assertThat(user).isEqualTo(user);
        }

        @Test
        @DisplayName("Should not be equal to null or different class")
        void shouldNotBeEqualToNullOrOtherTypes() {
            User user = new User(1L, "John", "john@example.com");

            assertThat(user).isNotEqualTo(null);
            assertThat(user).isNotEqualTo("some string");
        }
    }
}
