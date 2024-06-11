package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.exception.UserEmailAlreadyExistException;
import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.model.Address;
import com.sogeti.java.anonymous_artist.model.Cart;
import com.sogeti.java.anonymous_artist.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationService {

    private final UserService userService;
    private final AccountService accountService;
    private final AddressService addressService;
    private final CartService cartService;

    public RegistrationService(UserService userService, AccountService accountService, AddressService addressService, CartService cartService) {
        this.userService = userService;
        this.accountService = accountService;
        this.addressService = addressService;
        this.cartService = cartService;
    }

    public Account registerNewUserWithAccountAndRole(User newUser, Account newAccount, Address newAddress) {
        if (accountService.accountExist(newAccount.getEmail())) {
            throw new UserEmailAlreadyExistException(newAccount.getEmail());
        }
        Cart savedCart = cartService.saveCart(new Cart());
        User savedUser = userService.createUser(newUser);
        Account savedAccount = accountService.createAccount(newAccount);
        Address savedAddress = addressService.createAddress(newAddress);


        savedUser.setAccount(savedAccount);
        savedUser.setAddress(List.of(savedAddress));
        savedUser.setCart(savedCart);
        savedAccount.setUser(savedUser);
        savedAddress.setUser(savedUser);
        savedCart.setUser(savedUser);

        accountService.addAuthority(savedAccount.getEmail(), "ROLE_USER");

        return savedAccount;
    }
}
