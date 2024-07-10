package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.factory.UserFactory;
import com.sogeti.java.anonymous_artist.model.User;
import com.sogeti.java.anonymous_artist.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void givenNoConflicts_whenCreatingAnAddress_thenCreateAddressIsCreated() {
        // Given
        User createdUser = UserFactory.aUser().build();

        // When
        when(userRepository.save(any(User.class))).thenReturn(createdUser);

        User result = userService.createUser(createdUser);

        // Then
        assertEquals(result, createdUser);
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }
}
