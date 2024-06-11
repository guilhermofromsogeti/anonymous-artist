package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.request.AccountRequest;
import com.sogeti.java.anonymous_artist.response.AccountResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountMapper {

    public static Account fromAccountRequest(AccountRequest accountRequest) {
        return new Account(
                accountRequest.email(),
                accountRequest.password()
        );
    }

    public static AccountResponse toAccountResponse(Account account) {
        return new AccountResponse(
                account.getEmail()
        );
    }
}
