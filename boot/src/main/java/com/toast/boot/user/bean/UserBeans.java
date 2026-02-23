/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.user.bean;

import com.toast.boot.user.persistence.security.entity.Account;
import com.toast.boot.user.persistence.security.entity.Token;
import com.toast.boot.user.persistence.security.repository.AccountRepository;
import com.toast.boot.user.persistence.security.repository.TokenRepository;
import com.toast.user.app.repository.UserReadOnlyRepository;
import com.toast.user.app.response.UserInfoResponse;
import com.toast.user.domain.User;
import com.toast.user.domain.UserRepository;
import com.toast.user.domain.vo.UserId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class UserBeans {
    private static final Map<UUID, User> users = new ConcurrentHashMap<>();
    
    private static final Map<UUID, Account> accounts = new ConcurrentHashMap<>();
    
    private static final Map<UUID, Token> tokens = new ConcurrentHashMap<>();
    
    @Bean
    public AccountRepository accountRepository() {
        return new AccountRepository() {
            @Override
            public Optional<Account> findById(Long id) {
                return Optional.empty();
            }
            
            @Override
            public Optional<Account> findByEmail(String email) {
                return accounts.values().stream()
                               .filter(account -> account.getEmail().equals(email))
                               .findFirst();
            }
            
            @Override
            public boolean existsByEmail(String email) {
                return accounts.values().stream()
                               .anyMatch(account -> account.getEmail().equals(email));
            }
            
            @Override
            public Optional<Account> findByUserId(UUID userId) {
                return Optional.ofNullable(accounts.get(userId));
            }
            
            @Override
            public void save(Account account) {
                accounts.put(account.getUserId(), account);
            }
            
            @Override
            public void deleteById(Long userId) {
            }
        };
    }
    
    @Bean
    public UserReadOnlyRepository userReadOnlyRepository() {
        return new UserReadOnlyRepository() {
            @Override
            public Optional<UserInfoResponse> search(UUID id) {
                return Optional.ofNullable(users.get(id))
                               .map(user -> new UserInfoResponse(
                                       user.id().value(),
                                       user.name().firstName(),
                                       user.name().lastName(),
                                       user.age().value()
                               ));
            }
            
            @Override
            public List<UserInfoResponse> all() {
                return users.values().stream()
                               .map(user -> new UserInfoResponse(
                                       user.id().value(),
                                       user.name().firstName(),
                                       user.name().lastName(),
                                       user.age().value()
                               ))
                               .toList();
            }
        };
    }
    
    @Bean
    public UserRepository userRepository() {
        return new UserRepository() {
            @Override
            public void save(User user) {
                users.put(user.id().value(), user);
            }
            
            @Override
            public Optional<User> findById(UserId id) {
                return Optional.ofNullable(users.get(id.value()));
            }
        };
    }
    
    @Bean
    public TokenRepository tokenRepository() {
        return new TokenRepository() {
            @Override
            public void save(Token token) {
                tokens.put(token.getId(), token);
            }
            
            @Override
            public Optional<Token> findById(UUID id) {
                return Optional.ofNullable(tokens.get(id));
            }
            
            @Override
            public void revoke(UUID id) {
                tokens.remove(id);
            }
        };
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
