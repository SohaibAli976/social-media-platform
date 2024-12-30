package com.social_media_platform.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private String secretKey = "your_secret_key"; // Use environment variable or vault in production
    private long validityInMilliseconds = 3600000; // 1 hour validity for the JWT token

    // Generate JWT token
    public String generateToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Extract Claims from Token to get any claim
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract username from token
    public String getUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            // Checking token's validity and expiration in one method
            Claims claims = extractClaims(token);
            return !isTokenExpired(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Check if the token is expired
    public boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    // You can also add a utility method to extract any custom claim if needed
    public <T> T extractClaim(String token, ClaimsExtractor<T> extractor) {
        Claims claims = extractClaims(token);
        return extractor.extract(claims);
    }

    // Simple interface for extracting custom claims
    public interface ClaimsExtractor<T> {
        T extract(Claims claims);
    }
}
