package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.factory.UserFactory;
import com.sogeti.java.anonymous_artist.model.User;
import com.sogeti.java.anonymous_artist.request.UserRequest;
import com.sogeti.java.anonymous_artist.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    @MockBean
    UserMapper userMapper;

    @Test
    void givenUserRequest_whenMappedToUser_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        UserRequest userRequest = UserFactory.aUserRequest().build();

        // When
        User user = UserMapper.fromUserRequest(userRequest);

        // Then
        assertEquals(user.getFirstName(), userRequest.firstName());
        assertEquals(user.getInfix(), userRequest.infix());
        assertEquals(user.getLastName(), userRequest.lastName());
        assertEquals(user.getDateOfBirth(), userRequest.dateOfBirth());
        assertEquals(user.getPhoneNumber(), userRequest.phoneNumber());
    }

    @Test
    void givenUser_whenMappedToUserResponse_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        User user = UserFactory.aUser().build();

        // When
        UserResponse userResponse = UserMapper.toUserResponse(user);

        // Then
        assertEquals(user.getMembershipId(), userResponse.membershipId());
        assertEquals(user.getFirstName(), userResponse.firstName());
        assertEquals(user.getInfix(), userResponse.infix());
        assertEquals(user.getLastName(), userResponse.lastName());
        assertEquals(user.getDateOfBirth(), userResponse.dateOfBirth());
        assertEquals(user.getPhoneNumber(), userResponse.phoneNumber());
    }
}
