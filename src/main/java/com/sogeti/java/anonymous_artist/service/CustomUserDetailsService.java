package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.exception.NoDataFoundException;
import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.model.Authority;
import com.sogeti.java.anonymous_artist.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<Account> existingAccount = accountRepository.findById(email);
        if (existingAccount.isEmpty()) {
            throw new NoDataFoundException("We could not find the data that you were looking for. Please use an existing user ID or email.");
        }

        Account account = existingAccount.get();
        String password = account.getPassword();

        Set<Authority> authorities = existingAccount.get().getAuthorities();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Authority authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return new org.springframework.security.core.userdetails.User(email, password, grantedAuthorities);
    }
}
