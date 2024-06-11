package com.sogeti.java.anonymous_artist.service;
import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.model.Authority;
import com.sogeti.java.anonymous_artist.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;


    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean accountExist(String email) {
        return accountRepository.existsById(email);
    }


    public void addAuthority(String email, String authority) {
        // TODO: exception gooien
        Account account = accountRepository.findById(email).orElseThrow();
        account.addAuthority(new Authority(email, authority));

        accountRepository.save(account);
    }

    public Account createAccount(Account newAccount) {
        String encodedPassword = passwordEncoder.encode(newAccount.getPassword());
        Account createNewAccount = new Account(
                newAccount.getEmail(),
                encodedPassword);

        return accountRepository.save(createNewAccount);
    }
}
