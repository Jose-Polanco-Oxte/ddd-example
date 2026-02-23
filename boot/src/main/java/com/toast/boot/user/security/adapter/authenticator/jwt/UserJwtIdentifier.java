/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.user.security.adapter.authenticator.jwt;

import com.toast.boot.user.persistence.security.entity.Account;
import com.toast.boot.user.persistence.security.repository.AccountRepository;
import com.toast.boot.user.security.adapter.UserCredentials;
import com.toast.security.Identifier;
import com.toast.user.domain.User;
import com.toast.user.domain.UserRepository;
import com.toast.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserJwtIdentifier implements Identifier<Account, User, UserCredentials> {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoderService;
    
    @Override
    public Account identify(UserCredentials credentials) {
        Account account = accountRepository.findByEmail(credentials.email())
                                  .orElseThrow(() -> new RuntimeException("Account not found"));
        if (!encoderService.matches(credentials.password(), account.getHashedPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return account;
    }
    
    @Override
    public void link(UserCredentials credentials, User aggregate) {
        if (accountRepository.existsByEmail(credentials.email())) {
            throw new RuntimeException("Email already in use");
        }
        Account account = Account.builder()
                                  .email(credentials.email())
                                  .hashedPassword(encoderService.encode(credentials.password()))
                                  .userId(aggregate.id().value())
                                  .status("USER")
                                  .build();
        accountRepository.save(account);
    }
    
    @Override
    public void unlink(Account principal) {
        accountRepository.deleteById(principal.getId());
    }
    
    @Override
    public User bind(String domainId) {
        UserId userId = UserId.fromString(domainId);
        return userRepository.findById(userId)
                       .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
