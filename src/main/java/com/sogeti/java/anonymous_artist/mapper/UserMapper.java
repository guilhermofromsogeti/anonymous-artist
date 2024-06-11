package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.model.User;
import com.sogeti.java.anonymous_artist.request.UserRequest;
import com.sogeti.java.anonymous_artist.response.UserResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User fromUserRequest(UserRequest userRequest) {
        return new User(
                userRequest.firstName(),
                userRequest.infix(),
                userRequest.lastName(),
                userRequest.dateOfBirth(),
                userRequest.phoneNumber());
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getMembershipId(),
                user.getFirstName(),
                user.getInfix(),
                user.getLastName(),
                user.getDateOfBirth(),
                user.getPhoneNumber()
        );
    }
}
