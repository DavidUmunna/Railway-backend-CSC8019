package org.coffeeshop.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenService {

    private static final int MIN_HMAC_KEY_BYTES = 32;

    private final SecretKey key;
    private final long expirationSeconds;

    public JwtTokenService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-seconds:3600}") long expirationSeconds) {
        this.key = Keys.hmacShaKeyFor(validateSecret(secret));
        this.expirationSeconds = expirationSeconds;
    }

    private static byte[] validateSecret(String secret) {
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid JWT configuration: 'jwt.secret' must be provided and must not be blank.");
        }
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < MIN_HMAC_KEY_BYTES) {
            throw new IllegalArgumentException(
                    "Invalid JWT configuration: 'jwt.secret' must be at least "
                            + MIN_HMAC_KEY_BYTES
                            + " bytes when encoded as UTF-8 for HMAC signing.");
        }
        return secretBytes;
    }

    public String generateToken(String username, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expirationSeconds)))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
