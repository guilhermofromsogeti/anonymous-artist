package com.sogeti.java.anonymous_artist.handler;

import com.sogeti.java.anonymous_artist.exception.NoDataFoundException;
import com.sogeti.java.anonymous_artist.exception.ProductNotFoundException;
import com.sogeti.java.anonymous_artist.exception.TokenInvalidationException;
import com.sogeti.java.anonymous_artist.exception.UserEmailAlreadyExistException;
import com.sogeti.java.anonymous_artist.exception.handler.ApplicationExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationExceptionHandlerTest {

    @Mock
    private NoDataFoundException noDataFoundException;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private UserEmailAlreadyExistException userEmailAlreadyExistException;

    @Mock
    private BadCredentialsException badCredentialsException;

    @Mock
    private AuthenticationException authenticationException;

    @Mock
    private BindingResult bindingResult;


    @Test
    void givenInvalidArgument_whenMethodArgumentNotValidExceptionIsPassed_thenBadRequestStatusIsGivenAndErrorMessageIsPassed() {

        // Given
        FieldError fieldError = new FieldError("objectName", "fieldName", "error message");

        // When
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(java.util.Collections.singletonList(fieldError));
        ResponseEntity<Map<String, String>> responseEntity = ApplicationExceptionHandler.handleInvalidArgument(methodArgumentNotValidException);

        Map<String, String> expectedErrorMap = new HashMap<>();
        expectedErrorMap.put("timestamp", String.valueOf(LocalDate.now()));
        expectedErrorMap.put("fieldName", "error message");

        // Then
        assertEquals(expectedErrorMap, responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void givenProductsNotFoundException_whenHandled_thenReturnsNotFoundResponse() {
        // Given
        ProductNotFoundException exception = new ProductNotFoundException();

        // When
        ResponseEntity<String> responseEntity = ApplicationExceptionHandler.handleProductNotFoundException(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Product not found", responseEntity.getBody());
    }

    @Test
    void givenInvalidArgument_whenNoDataFoundExceptionIsThrown_thenNotFoundStatusIsGivenAndErrorMessageIsPassed() {
        // When
        ResponseEntity<String> responseEntity = ApplicationExceptionHandler.handleNoDatFoundException();

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("We could not find the data that you were looking for. Please use an existing user ID or email.", responseEntity.getBody());
    }

    @Test
    void givenInvalidArgument_whenUserEmailAlreadyExistExceptionIsThrown_thenHttpStatusConflictIsGivenAndErrorMessageIsPassed() {
        // When
        ResponseEntity<String> responseEntity = ApplicationExceptionHandler.handleUserEmailAlreadyExistException();

        // Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("This email address is already used. Please fill in another email address.", responseEntity.getBody());
    }

    @Test
    void givenInvalidArgument_whenBadCredentialsExceptionIsThrown_thenHttpStatusUnAuthorizedIsGivenAndErrorMessageIsPassed() {
        // When
        ResponseEntity<String> responseEntity = ApplicationExceptionHandler.handleBadCredentialsOnAuthorisation();

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Incorrect email address and/or password, please enter a valid email address (example@example.com) and password.", responseEntity.getBody());
    }

    @Test
    void givenInvalidArgument_whenAuthenticationExceptionIsThrown_thenHttpStatusUnAuthorizedIsGivenAndErrorMessageIsPassed() {
        // When
        ResponseEntity<String> response = ApplicationExceptionHandler.handleAuthenticationException();

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("You are not authorized. Please contact an administrator.", response.getBody());
    }

    @Test
    void givenInvalidArgument_whenProductsAlreadyExistExceptionIsThrown_thenHttpStatusValue409IsGivenAndErrorMessageIsPassed() {
        // When
        ResponseEntity<String> response = ApplicationExceptionHandler.handleProductAlreadyExistException();

        // Then
        assertEquals(HttpStatus.valueOf(409), response.getStatusCode());
        assertEquals("The id (product) you provided is already registered. Please register a new product.", response.getBody());
    }

    @Test
    void givenHttpMessageNotReadableException_whenHttpMessageNotReadableExceptionIsThrown_thenHttpStatusBadRequestIsGivenAndErrorMessageIsPassed() {
        // Given
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("");

        // When
        ResponseEntity<String> response = ApplicationExceptionHandler.handleHttpMessageNotReadableException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid request format: " + exception.getMessage(), response.getBody());
    }

    @Test
    void givenTokenInvalidationException_whenHttpMessageNotReadableExceptionIsThrown_thenHttpStatusUnauthorizedIsGivenAndErrorMessageIsPassed() {
        // Given
        TokenInvalidationException exception = new TokenInvalidationException();

        // When
        ResponseEntity<String> response = ApplicationExceptionHandler.handleTokenInvalidationException(exception);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Cannot find data: The token is no longer valid. Please try to login again.", response.getBody());
    }
}
