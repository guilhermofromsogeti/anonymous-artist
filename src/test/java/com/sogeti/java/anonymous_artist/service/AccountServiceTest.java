package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.factory.AccountFactory;
import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AccountService accountService;


    @Test
    void givenExistingAccount_whenAccountExist_thenReturnsTrue() {
        // Given
        String existingEmail = "existing@example.com";
        when(accountRepository.existsById(existingEmail)).thenReturn(true);

        // When
        boolean result = accountService.accountExist(existingEmail);

        // Then
        assertTrue(result);
    }

    @Test
    void givenNonExistingAccount_whenAccountExist_thenReturnsFalse() {
        // Given
        String nonExistingEmail = "nonexisting@example.com";
        when(accountRepository.existsById(nonExistingEmail)).thenReturn(false);

        // When
        boolean result = accountService.accountExist(nonExistingEmail);

        // Then
        assertFalse(result);
    }

    @Test
    void givenNoConflicts_whenAddingAuthority_ThenAuthorityIsAddedToAccount() {
        // Given
        Account existingAccount = AccountFactory.anAccount().build();
        when(accountRepository.save(any(Account.class))).thenReturn(existingAccount);
        when(accountRepository.findById(existingAccount.getEmail())).thenReturn(Optional.of(existingAccount));

        // When
        accountService.addAuthority(existingAccount.getEmail(), "ROLE_USER");

        // Then
        verify(accountRepository, times(1)).findById(existingAccount.getEmail());
        verify(accountRepository, times(1)).save(existingAccount);
        verify(accountRepository).save(any(Account.class));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void givenNoConflicts_whenCreatingAnAccount_thenANewAccountIsCreated() {
        // Given
        Account account = AccountFactory.anAccount().build();
        Account accountToCreate = AccountFactory.anAccount()
                .email(account.getEmail())
                .password(account.getPassword())
                .build();

        when(passwordEncoder.encode(account.getPassword())).thenReturn(account.getPassword());
        when(accountRepository.save(any(Account.class))).thenReturn(accountToCreate);

        // When
        Account result = accountService.createAccount(account);

        // Then
        assertThat(result).isEqualTo(accountToCreate);
        verify(accountRepository).save(any(Account.class));
        verifyNoMoreInteractions(accountRepository);
    }
}
