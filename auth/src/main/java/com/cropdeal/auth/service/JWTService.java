package com.cropdeal.auth.service;

import com.cropdeal.auth.exception.JWTServiceException;
import com.cropdeal.auth.modal.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);

    private static final String JWT_SECRET = "cfd7a88c9f1460f1ada7b08cf7ad9677f56612935b49af533fbf1b2daa3b887cf8fb091d140c5599ff5512b8b22663d663ab88465c96d3d0b7ba7a8ce1e26c47";

    public String generateToken(String username, Role role) {
        try {
            logger.info("Generating token for user: {}", username);

            Map<String, Object> claims = new HashMap<>();
            claims.put("role", role.name()); // Add role to claims

            return Jwts.builder()
                    .claims()
                    .add(claims)
                    .subject(username)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Token valid for 30 minutes
                    .and()
                    .signWith(getKey())
                    .compact();
        } catch (Exception e) {
            logger.error("Token generation failed: {}", e.getMessage());
            throw new JWTServiceException("Failed to generate token");
        }
    }

    public SecretKey getKey() {
        try {
            logger.info("Generating secret key for JWT");
            byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            logger.error("Error generating JWT key: {}", e.getMessage());
            throw new JWTServiceException("Invalid JWT key");
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            logger.info("Extracting claims from token");
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            logger.error("Error extracting claims: {}", e.getMessage());
            throw new JWTServiceException("Invalid token");
        }
    }

    public void validateToken(final String token) {
        try {
            logger.info("Validating token...");
            if (isTokenExpired(token)) {
                throw new JWTServiceException("Token has expired");
            }
            logger.info("Token validation successful");
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            throw new JWTServiceException("Invalid token");
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUserRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
}