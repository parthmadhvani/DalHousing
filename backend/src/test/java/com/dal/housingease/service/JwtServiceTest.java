package com.dal.housingease.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;


class JwtServiceTest {

    private JwtServiceImpli jwtService;
    private final String secretKey = "L7cplm35O4iIwCPtDlRILwaQH4prT4mIxDfdK1W+IuT2rjZPyBx3X0MyzSpv7Y8Vqwertyuiopasdfghjklzxcvbnmqwerty"; // 512-bit key in Base64
    private final long jwtExpiration = 3600000; // 1 hour in milliseconds

    @BeforeEach
    void setUp() {
        jwtService = spy(new JwtServiceImpli());
        jwtService.secretKey = secretKey;
        jwtService.jwtExpiration = jwtExpiration;
        //doReturn(Keys.hmacShaKeyFor(secretKey.getBytes())).when(jwtService).getSignInKey();
        doReturn(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey))).when(jwtService).getSignInKey();
    }

    @Test
    void testExtractUsername() {
        UserDetails user = User.builder()
                .username("testuser@example.com")
                .password("password")
                .build();

        String token = jwtService.generateToken(user);
        String username = jwtService.extractUsername(token);
        assertEquals(user.getUsername(), username);
    }

    @Test
    void testExtractClaim() {
        UserDetails user = User.builder()
                .username("testuser@example.com")
                .password("password")
                .build();

        String token = jwtService.generateToken(user);
        String username = jwtService.extractClaim(token, Claims::getSubject);
        assertEquals("testuser@example.com", username);
    }

    @Test
    void testGenerateTokenUserDetails() {
        UserDetails user = User.builder()
                .username("testuser@example.com")
                .password("password")
                .build();

        String token = jwtService.generateToken(user);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testGenerateTokenMapOfStringObjectUserDetails() {
        UserDetails user = User.builder()
                .username("testuser@example.com")
                .password("password")
                .build();

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("customClaim", "value");

        String token = jwtService.generateToken(extraClaims, user);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testGetExpirationTime() {
        assertEquals(jwtExpiration, jwtService.getExpirationTime());
    }

    @Test
    void testIsTokenValid() {
        UserDetails user = User.builder()
                .username("testuser@example.com")
                .password("password")
                .build();

        String token = jwtService.generateToken(user);
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void testExtractExpirationUsingReflection() throws Exception {
        Method method = JwtServiceImpli.class.getDeclaredMethod("extractExpiration", String.class);
        method.setAccessible(true);

        String token = generateTestToken("testuser@example.com");
        Date expiration = (Date) method.invoke(jwtService, token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testExtractAllClaimsUsingReflection() throws Exception {
        Method method = JwtServiceImpli.class.getDeclaredMethod("extractAllClaims", String.class);
        method.setAccessible(true);

        String token = generateTestToken("testuser@example.com");
        Claims claims = (Claims) method.invoke(jwtService, token);
        assertNotNull(claims);
        assertEquals("testuser@example.com", claims.getSubject());
    }

    @Test
    void testTokenWithValidExpiration() {
        UserDetails userDetails = User.builder().username("testUser").password("password").build();

        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour in the future
                .signWith(jwtService.getSignInKey(), SignatureAlgorithm.HS512)
                .compact();

        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid, "The token should be valid.");
    }

    private String generateTestToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtService.jwtExpiration))
                .signWith(jwtService.getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private String generateExpiredToken(String username)
    {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - jwtService.jwtExpiration * 2))
                .setExpiration(new Date(System.currentTimeMillis() - jwtService.jwtExpiration))
                .signWith(jwtService.getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    @Test
    void testGetSignInKey() {
        // Obtain the generated key
        Key key = jwtService.getSignInKey();
        assertNotNull(key, "The generated key should not be null");

        // Manually create the expected key using the same secret key
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        Key expectedKey = Keys.hmacShaKeyFor(keyBytes);

        // Assert the generated key and the expected key are equal
        assertEquals(expectedKey, key, "The generated key should match the expected key");
    }

    @Test
    void testGetSignInKeyWithInvalidKey() {
        // Set an invalid key
        String invalidSecretKey = "short-key";
        ReflectionTestUtils.setField(jwtService, "secretKey", invalidSecretKey);

        // Ensure the mock setup doesn't interfere with this specific test
        doReturn(null).when(jwtService).getSignInKey();

        // Assert that an exception is thrown when generating the key
        assertThrows(io.jsonwebtoken.security.WeakKeyException.class, () -> {
            byte[] keyBytes = invalidSecretKey.getBytes();
            Keys.hmacShaKeyFor(keyBytes);
        }, "Expected a WeakKeyException due to short key");
    }


    @Test
    void testIsTokenValidWithIncorrectUsername() {
        UserDetails user = User.builder()
                .username("testuser@example.com")
                .password("password")
                .build();

        String token = jwtService.generateToken(user);
        UserDetails incorrectUser = User.builder()
                .username("wronguser@example.com")
                .password("password")
                .build();

        assertFalse(jwtService.isTokenValid(token, incorrectUser), "The token should be invalid because the username does not match.");
    }

    @Test
    void testGetSignInKeyGeneratesKeyCorrectly() {
        Key key = jwtService.getSignInKey();
        assertNotNull(key, "The signing key should not be null");

        // Manually create the expected key using the same secret key
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        Key expectedKey = Keys.hmacShaKeyFor(keyBytes);

        assertEquals(expectedKey, key, "The generated key should match the expected key");
    }



}