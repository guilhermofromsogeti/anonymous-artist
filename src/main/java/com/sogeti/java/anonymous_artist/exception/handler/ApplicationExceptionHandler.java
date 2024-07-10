package com.sogeti.java.anonymous_artist.exception.handler;


import com.sogeti.java.anonymous_artist.exception.NoDataFoundException;
import com.sogeti.java.anonymous_artist.exception.ProductAlreadyExistException;
import com.sogeti.java.anonymous_artist.exception.ProductNotFoundException;
import com.sogeti.java.anonymous_artist.exception.TokenInvalidationException;
import com.sogeti.java.anonymous_artist.exception.UserEmailAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler extends RuntimeException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public static ResponseEntity<Map<String, String>> handleInvalidArgument(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put("timestamp", String.valueOf(LocalDate.now()));
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public static ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException exception) {
        return new ResponseEntity<>(exception.getReason(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public static ResponseEntity<String> handleNoDatFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("We could not find the data that you were looking for. Please use an existing user ID or email.");
    }

    @ExceptionHandler(UserEmailAlreadyExistException.class)
    public static ResponseEntity<String> handleUserEmailAlreadyExistException() {
        return ResponseEntity.status(HttpStatusCode.valueOf(409)).body("This email address is already used. Please fill in another email address.");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public static ResponseEntity<String> handleBadCredentialsOnAuthorisation() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect email address and/or password, please enter a valid email address (example@example.com) and password.");
    }

    @ExceptionHandler(AuthenticationException.class)
    public static ResponseEntity<String> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized. Please contact an administrator.");
    }

    @ExceptionHandler(ProductAlreadyExistException.class)
    public static ResponseEntity<String> handleProductAlreadyExistException() {
        return ResponseEntity.status(HttpStatus.valueOf(409)).body("The id (product) you provided is already registered. Please register a new product.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public static ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Invalid request format: " + ex.getMessage());
    }

    @ExceptionHandler(TokenInvalidationException.class)
    public static ResponseEntity<String> handleTokenInvalidationException(TokenInvalidationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }


}


