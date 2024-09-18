package com.dal.housingease.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;
/**
 * JwtService interface provides methods for handling JWT tokens.
 * This includes extracting claims, generating tokens, validating tokens,
 * and retrieving the expiration time.
 */
public interface JwtService 
{
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    long getExpirationTime();
    boolean isTokenValid(String token, UserDetails userDetails);
}
