package com.sogeti.java.anonymous_artist.factory;

import com.github.javafaker.Faker;
import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.model.Authority;
import com.sogeti.java.anonymous_artist.request.AccountRequest;
import com.sogeti.java.anonymous_artist.response.AccountResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountFactory {

    private final static Faker faker = new Faker();

    public static Account.AccountBuilder anAccount() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();

        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority(email, "ROLE_USER"));

        return Account.builder()
                .email(email)
                .password(password)
                .authorities(authorities);
    }

    public static AccountRequest.AccountRequestBuilder aRequest() {
        return AccountRequest.builder()
                .email(faker.internet().emailAddress())
                .password("DoeMaarWat123#");
    }

    public static Account fromAccountRequestToAccount(AccountRequest accountRequest) {
        return new Account(
                accountRequest.email(),
                accountRequest.password()
        );
    }

    public static AccountResponse fromAccountToAccountResponse(Account account) {
        return new AccountResponse(
                account.getEmail());
    }
}
