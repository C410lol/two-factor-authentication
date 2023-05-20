package com.ms.auth.authmicroservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    public String generateToken(String username) {
        return "Bearer " + Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 5*60*1000))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication tryToAuthenticate(String token) {
        if(isNotExpired(token)) {
            return new UsernamePasswordAuthenticationToken(
                    getSubject(token),
                    null,
                    null
            );
        }
        return null;
    }

    public String getSubject(String token) {
        return getClaim(token, Claims::getSubject);
    }

    private boolean isNotExpired(String token) {
        return getClaim(token, Claims::getExpiration).after(
                new Date(System.currentTimeMillis()));
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build().parseClaimsJws(token)
                .getBody();
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(getAllClaims(token));
    }

    private @NotNull Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

}
