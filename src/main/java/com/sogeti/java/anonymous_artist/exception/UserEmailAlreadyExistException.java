package com.sogeti.java.anonymous_artist.exception;

public class UserEmailAlreadyExistException extends RuntimeException {
    public UserEmailAlreadyExistException(String email) {
        super(String.format("User with email '%s' already exists", email));
    }
}
