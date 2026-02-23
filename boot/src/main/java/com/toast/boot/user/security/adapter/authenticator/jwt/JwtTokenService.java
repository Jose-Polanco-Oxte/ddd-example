/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.user.security.adapter.authenticator.jwt;

import com.toast.boot.shared.utils.TimeUtils;
import com.toast.boot.user.persistence.security.entity.Account;
import com.toast.boot.user.persistence.security.entity.Token;
import com.toast.boot.user.persistence.security.repository.AccountRepository;
import com.toast.boot.user.persistence.security.repository.TokenRepository;
import com.toast.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final KeyPair keyPair = Jwts.SIG.ES256.keyPair().build();
    private final TokenRepository tokenRepository;
    private final AccountRepository accountRepository;
    @Value("${jwt.access-expiration-minutes}")
    private long accessExpiration;
    @Value("${jwt.refresh-expiration-days}")
    private long refreshExpiration;
    
    public Account parseAccessToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                                        .verifyWith(keyPair.getPublic())
                                        .build()
                                        .parseSignedClaims(token);
        Claims claims = claimsJws.getPayload();
        return Account.builder()
                       .userId(UUID.fromString(claims.getSubject()))
                       .email(claims.get("email", String.class))
                       .status(claims.get("status", String.class))
                       .build();
    }
    
    public JwtRefreshResponse refreshToken(String refreshToken) {
        String plainToken = new String(Base64.getDecoder().decode(refreshToken));
        String[] parts = plainToken.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid refresh token format");
        }
        UUID tokenId = UUID.fromString(parts[0]);
        
        Token token = tokenRepository.findById(tokenId).orElseThrow(() -> {
            tokenRepository.revoke(tokenId);
            return new IllegalArgumentException("Refresh token not found");
        });
        
        if (token.getExpiresAt().isBefore(Instant.now())) {
            tokenRepository.revoke(tokenId);
            throw new IllegalArgumentException("Refresh token has expired");
        }
        
        Account account = accountRepository.findById(token.getAccountId()).orElseThrow(() -> {
            tokenRepository.revoke(tokenId);
            return new IllegalArgumentException("Account not found for refresh token");
        });
        
        String accessToken = generateAccessToken(account);
        return new JwtRefreshResponse(accessToken);
    }
    
    public JwtPairResponse generatePairTokens(Account account) {
        String accessToken = generateAccessToken(account);
        String refreshToken = generateRefreshToken(account);
        return new JwtPairResponse(accessToken, refreshToken);
    }
    
    private String generateAccessToken(Account account) {
        return Jwts.builder()
                       .id(UUID.randomUUID().toString())
                       .issuer(User.class.getSimpleName())
                       .subject(account.getUserId().toString())
                       .audience().add("toast:ddd-example").and()
                       .claim("status", account.getStatus())
                       .claim("email", account.getEmail())
                       .issuedAt(new Date())
                       .notBefore(new Date())
                       .expiration(new Date(System.currentTimeMillis() + TimeUtils.minutesToMillis(accessExpiration)))
                       .signWith(keyPair.getPrivate())
                       .compact();
    }
    
    private String generateRefreshToken(Account account) {
        UUID uuid = UUID.randomUUID();
        Instant expiration = Instant.now().plusMillis(TimeUtils.daysToMillis(refreshExpiration));
        String plainToken = uuid + ":" + expiration.toString();
        Token token = Token.builder()
                              .id(uuid)
                              .accountId(account.getId())
                              .value(plainToken)
                              .expiresAt(expiration)
                              .build();
        tokenRepository.save(token);
        return Base64.getEncoder().encodeToString(plainToken.getBytes());
    }
}
