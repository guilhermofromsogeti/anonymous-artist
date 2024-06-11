package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.exception.NoDataFoundException;
import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.repository.AccountRepository;
import com.sogeti.java.anonymous_artist.request.AccountRequest;
import com.sogeti.java.anonymous_artist.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    JwtUtil jwtUtil;
    private CustomUserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;

    private final AccountRepository accountRepository;

    public AuthenticationService(CustomUserDetailsService userDetailsService, JwtUtil jwtUtil, AuthenticationManager authenticationManager, AccountRepository accountRepository) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
    }

    public final String generateJwt(String email) {
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(email);
        return jwtUtil.generateToken(userDetails);
    }

    public void authenticateUser(AccountRequest accountRequest) {
        String email = accountRequest.email();
        String password = accountRequest.password();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (AuthenticationException authenticationException) {
            throw new BadCredentialsException("Incorrect email address or password, please enter a valid email or password", authenticationException);
        }
    }

    public String getFirstNameOfUserByEmail(String email) {
        Optional<Account> optionalAccount = accountRepository.findById(email);
        if (optionalAccount.isEmpty()) {
            throw new NoDataFoundException("We could not find the data that you were looking for. Please use an existing user ID or email.");
        }
        return optionalAccount.get().getUser().getFirstName();
    }
}
