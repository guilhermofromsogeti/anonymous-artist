package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.model.User;
import com.sogeti.java.anonymous_artist.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User newUser) {

        User createNewUser = new User(
                newUser.getFirstName(),
                newUser.getInfix(),
                newUser.getLastName(),
                newUser.getDateOfBirth(),
                newUser.getPhoneNumber());
        return userRepository.save(createNewUser);
    }
}
