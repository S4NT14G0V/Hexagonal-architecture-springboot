package com.backend.hexagonal.domain.model;

import com.backend.hexagonal.domain.exception.InvalidUserDataException;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class User {

    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private UUID id;
    private String name;
    private String email;

    public User(String name, String email) {
        validateName(name);
        validateEmail(email);
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
    }

    public User(UUID id, String name, String email) {
        validateName(name);
        validateEmail(email);
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        validateName(name);
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void updateEmail(String email) {
        validateEmail(email);
        this.email = email;
    }

    public void update(String name, String email) {
        validateName(name);
        validateEmail(email);
        this.name = name;
        this.email = email;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidUserDataException("User name cannot be null or empty");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidUserDataException("User email cannot be null or empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidUserDataException("User email format is invalid");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user)) {
            return false;
        }
        if (id == null || user.id == null) {
            return false;
        }
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + "]";
    }
}
