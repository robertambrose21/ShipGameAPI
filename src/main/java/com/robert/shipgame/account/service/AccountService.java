package com.robert.shipgame.account.service;

import com.robert.shipgame.account.AccountMapper;
import com.robert.shipgame.account.data.AccountDAO;
import com.robert.shipgame.account.data.AccountRepository;
import com.robert.shipgame.account.exception.AccountException;
import com.robert.shipgame.account.exception.AccountNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public Account createAccount(final Account account) {
        if(account.id() != null) {
            throw new AccountException("Cannot create account with id already supplied");
        }

        final Optional<Account> existingAccount = getAccountByOAuth2Sub(account.oauth2Sub());

        if(existingAccount.isPresent()) {
            throw new AccountException("Cannot create account, existing account exists with id: " + account.oauth2Sub());
        }

        return AccountMapper.INSTANCE.daoToPojo(accountRepository.save(AccountMapper.INSTANCE.pojoToDAO(account)));
    }

    @Transactional
    public Account updateAccount(final Account account) {
        if(account.id() == null) {
            throw new AccountNotFoundException("Cannot update account with blank id");
        }

        final AccountDAO accountToUpdate = accountRepository.findById(account.id()).orElseThrow(() ->
                new AccountNotFoundException("Cannot find account with id " + account.id()));

        if(!account.oauth2Sub().equals(accountToUpdate.getOauth2Sub())) {
            throw new AccountException("Account sub " + account.oauth2Sub() + " does not match existing sub for account");
        }

        accountToUpdate.setEmail(account.email());
        accountToUpdate.setFirstName(account.firstName());
        accountToUpdate.setLastName(account.lastName());

        return AccountMapper.INSTANCE.daoToPojo(accountRepository.save(accountToUpdate));
    }

    public Optional<Account> getAccountByOAuth2Sub(final String oauth2Sub) {
        return accountRepository.findByOAuth2Sub(oauth2Sub).map(AccountMapper.INSTANCE::daoToPojo);
    }

    public Optional<Account> getLoggedInAccount() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final OidcUser principal = (OidcUser) authentication.getPrincipal();

        return getAccountByOAuth2Sub(principal.getSubject());
    }

    public Account createOrUpdateAccountFromPrincipal(final OidcUser principal) {
        final Account.AccountBuilder accountBuilder = Account.builder()
                .firstName(principal.getName())
                .lastName(principal.getFamilyName())
                .email(principal.getEmail())
                .oauth2Sub(principal.getSubject());

        final Optional<Account> existingAccount = getAccountByOAuth2Sub(principal.getSubject());

        if(existingAccount.isEmpty()) {
            return createAccount(accountBuilder.build());
        }

        return updateAccount(
                accountBuilder
                        .id(existingAccount.get().id())
                        .build());
    }

}
