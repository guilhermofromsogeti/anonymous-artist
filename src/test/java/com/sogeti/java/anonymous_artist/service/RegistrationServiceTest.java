package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.exception.UserEmailAlreadyExistException;
import com.sogeti.java.anonymous_artist.factory.AccountFactory;
import com.sogeti.java.anonymous_artist.factory.AddressFactory;
import com.sogeti.java.anonymous_artist.factory.CartFactory;
import com.sogeti.java.anonymous_artist.factory.UserFactory;
import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.model.Address;
import com.sogeti.java.anonymous_artist.model.Cart;
import com.sogeti.java.anonymous_artist.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private AccountService accountService;
    @Mock
    private AddressService addressService;

    @Mock
    private CartService cartService;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    @WithMockUser
    void givenNewUserNewAccountNewAddress_whenAccountDoesNotExist_thenUserAccountAddressAreCreatedCouplingIsSetAndUserRoleIsAdded() {
        // Given
        User newUser = UserFactory.aUser().build();
        Account newAccount = AccountFactory.anAccount().build();
        Address newAddress = AddressFactory.anAddress().build();

        // When
        when(accountService.accountExist(newAccount.getEmail())).thenReturn(false);

        Cart savedCart = CartFactory.aCart().build();
        when(cartService.saveCart(any(Cart.class))).thenReturn(savedCart);

        User savedUser = UserFactory.aUser().membershipId(newUser.getMembershipId()).build();
        when(userService.createUser(newUser)).thenReturn(savedUser);

        Account savedAccount = AccountFactory.anAccount().email(newAccount.getEmail()).build();
        when(accountService.createAccount(newAccount)).thenReturn(savedAccount);

        Address savedAddress = AddressFactory.anAddress().addressId(newAddress.getAddressId()).build();
        when(addressService.createAddress(newAddress)).thenReturn(savedAddress);

        registrationService.registerNewUserWithAccountAndRole(newUser, newAccount, newAddress);

        // Then
        verify(cartService).saveCart(any(Cart.class));
        verify(userService).createUser(newUser);
        verify(accountService).createAccount(newAccount);
        verify(addressService).createAddress(newAddress);
        verify(accountService).addAuthority(newAccount.getEmail(), "ROLE_USER");
        verifyNoMoreInteractions(cartService, userService, accountService, addressService);

    }

    @Test
    void givenNewUserNewAccountNewAddress_whenAccountDoesExist_thenUserEmailAlreadyExistExceptionIsThrown() {
        // Given
        User newUser = UserFactory.aUser().build();
        Account newAccount = AccountFactory.anAccount().build();
        Address newAddress = AddressFactory.anAddress().build();

        // When
        when(accountService.accountExist(newAccount.getEmail())).thenReturn(true);
        UserEmailAlreadyExistException runtimeException = assertThrows(UserEmailAlreadyExistException.class, () -> {
            registrationService.registerNewUserWithAccountAndRole(newUser, newAccount, newAddress);
        });

        // Then
        assertEquals("User with email '" + newAccount.getEmail() + "' already exists", runtimeException.getMessage());
        verify(accountService).accountExist(newAccount.getEmail());
    }
}
