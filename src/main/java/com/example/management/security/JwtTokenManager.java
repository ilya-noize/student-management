package com.example.management.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Хранитель секретов:
 * - хранит ключ в памяти для генерации уникальных json web token-ов только для этого приложения.
 * - устанавливает срок действия токена.
 */
@Component
@Slf4j
public class JwtTokenManager {
    private final SecretKey key;
    private final long expirationTime;

    public JwtTokenManager(
            @Value("${jwt.secret-key}") String key,
            @Value("${jwt.lifetime}") long expirationTime
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
        this.expirationTime = TimeUnit.MINUTES.toMillis(expirationTime);
    }

    /**
     * Создаёт токены для сохранения состояния пользователя на сервере.
     */
    public String generateToken(String login) {
        log.info("Generating token");
        return Jwts.builder()
                .subject(login)
                .signWith(key)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .compact();
    }

    /**
     * Получает имя пользователя из токена
     */
    public String getLoginFromToken(String token) {
        String login = parseJWT(token).getSubject();
        log.info("{}, {}", "Getting user name from JsonWebTokens",login);
        return login;
    }

    public boolean isValid(String jwt) {
        log.info("Validating the validity of a JSON Web Token");
        return parseJWT(jwt).getExpiration().after(new Date());
    }

    public Claims parseJWT(String jwt) {
        log.info("Parsing JWT");
        assert !jwt.isEmpty();
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
