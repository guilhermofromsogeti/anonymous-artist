package com.sogeti.java.anonymous_artist.factory;

import com.github.javafaker.Faker;
import com.sogeti.java.anonymous_artist.model.User;
import com.sogeti.java.anonymous_artist.request.UserRequest;
import com.sogeti.java.anonymous_artist.response.UserResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFactory {

    private final static Faker faker = new Faker();

    static LocalDate startDate = LocalDate.of(1950, 1, 1);
    static LocalDate endDate = LocalDate.of(2022, 12, 31);
    static long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
    static long randomDays = faker.random().nextInt((int) daysBetween + 1);
    static LocalDate randomDate = startDate.plusDays(randomDays);


    public static User.UserBuilder aUser() {
        return User.builder()
                .membershipId(UUID.randomUUID())
                .firstName(faker.name().firstName())
                .infix(faker.name().prefix())
                .lastName(faker.name().lastName())
                .dateOfBirth(randomDate)
                .phoneNumber(faker.phoneNumber().phoneNumber());
    }

    public static UserRequest.UserRequestBuilder aUserRequest() {
        return UserRequest.builder()
                .firstName(faker.name().firstName())
                .infix(faker.name().prefix())
                .lastName(faker.name().lastName())
                .dateOfBirth(randomDate)
                .phoneNumber(faker.phoneNumber().phoneNumber());
    }


    public static User fromUserRequestToUser(UserRequest userRequest) {
        return new User(
                userRequest.firstName(),
                userRequest.infix(),
                userRequest.lastName(),
                userRequest.dateOfBirth(),
                userRequest.phoneNumber()
        );
    }

    public static UserResponse fromUserToUserResponse(User user) {
        return new UserResponse(
                user.getMembershipId(),
                user.getFirstName(),
                user.getInfix(),
                user.getLastName(),
                user.getDateOfBirth(),
                user.getPhoneNumber());
    }
}
