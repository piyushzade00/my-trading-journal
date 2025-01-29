package com.example.tradingjournal;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Securely generate a 256-bit key for HS256 algorithm
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // JWT expiration time (15 mins)
    private static final long EXPIRATION_TIME = 900000;

    // Refresh Token expiration time (15 Days)
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1296000000;

    // Method to generate a JWT token
    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // Email as the subject of the token
                .setIssuedAt(new Date()) // Token issue time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiration time
                .signWith(SECRET_KEY) // Sign the token with the generated secret key
                .compact(); // Create the JWT
    }

    public static String generateRefreshToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME)) // Set expiration time
                .signWith(SECRET_KEY)
                .compact();

    }

    // Method to validate the JWT token
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY) // Use the same key for validation
                    .build()
                    .parseClaimsJws(token); // Parse and validate the token
            return true;
        } catch (Exception e) {
            return false; // Invalid token
        }
    }

    // Method to get the username from the JWT token
    public static String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
