package com.dal.housingease.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
/**
 * Implementation of the JwtService interface.
 * Provides methods for handling JWT tokens, including extracting claims,
 * generating tokens, validating tokens, and retrieving the expiration time.
 */
@Setter
@RequiredArgsConstructor
@Service
public class JwtServiceImpli implements JwtService
{
	private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpli.class);
    @Value("${security.jwt.secret-key}") String secretKey;

    @Value("${security.jwt.expiration-time}") long jwtExpiration;
    /**
     * Extracts the username from the JWT token.
     *
     * @param token the JWT token.
     * @return the username extracted from the token.
     */
    public String extractUsername(String token)
    {
        logger.debug("Extracted username");
        return extractClaim(token, Claims::getSubject);

    }
    /**
     * Extracts a specific claim from the JWT token using the provided claims resolver function.
     *
     * @param token the JWT token.
     * @param claimsResolver the function to resolve the claim.
     * @param <T> the type of the claim to be extracted.
     * @return the extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) 
    {
        final Claims claims = extractAllClaims(token);
        logger.debug("Extracted claim");
        return claimsResolver.apply(claims);
    }
    /**
     * Generates a JWT token for the specified user details.
     *
     * @param userDetails the user details for which the token is to be generated.
     * @return the generated JWT token.
     */
    public String generateToken(UserDetails userDetails)
    {
        logger.debug("Generating token for user: {}", userDetails.getUsername());
        return generateToken(new HashMap<>(), userDetails);
    }
    /**
     * Generates a JWT token for the specified user details with additional claims.
     *
     * @param extraClaims additional claims to be included in the token.
     * @param userDetails the user details for which the token is to be generated.
     * @return the generated JWT token.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails)
    {
        logger.debug("Generating token with extra claims: {} for user: {}", extraClaims, userDetails.getUsername());
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }
    /**
     * Retrieves the expiration time for the JWT token.
     *
     * @return the expiration time in milliseconds.
     */
    public long getExpirationTime()
    {
        logger.debug("JWT expiration time: {}", jwtExpiration);
        return jwtExpiration;
    }
    /**
     * Builds the JWT token with specified claims, user details, and expiration time.
     *
     * @param extraClaims additional claims to be included in the token.
     * @param userDetails the user details for which the token is to be generated.
     * @param expiration the expiration time for the token.
     * @return the generated JWT token.
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    )
    {
        logger.debug("Building token with claims: {}, user: {}, expiration: {}", extraClaims, userDetails.getUsername(), expiration);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    /**
     * Validates the JWT token against the specified user details.
     *
     * @param token the JWT token to be validated.
     * @param userDetails the user details to be validated against.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        logger.debug("Validating token for user: {}", userDetails.getUsername());
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    /**
     * Checks if the JWT token is expired.
     *
     * @param token the JWT token to be checked.
     * @return true if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token)
    {
        logger.debug("Checking if token is expired.");
        return extractExpiration(token).before(new Date());
    }
    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token.
     * @return the expiration date extracted from the token.
     */
    private Date extractExpiration(String token)
    {
        logger.debug("Extracting expiration date from token.");
        return extractClaim(token, Claims::getExpiration);
    }
    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token.
     * @return the claims extracted from the token.
     */
    private Claims extractAllClaims(String token)
    {
        logger.debug("Extracting all claims from token.");
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    /**
     * Generates the signing key for JWT token using the secret key.
     *
     * @return the generated signing key.
     */
    Key getSignInKey()
    {
        logger.debug("Generating signing key.");
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
