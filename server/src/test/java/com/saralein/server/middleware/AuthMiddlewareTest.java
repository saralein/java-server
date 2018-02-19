package com.saralein.server.middleware;

import com.saralein.server.middleware.AuthMiddleware;
import org.junit.Before;
import org.junit.Test;

import java.util.Base64;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AuthMiddlewareTest {
    private AuthMiddleware authMiddleware;

    @Before
    public void setUp() {
        authMiddleware = new AuthMiddleware("admin", "hunter2");
    }

    @Test
    public void returnsFalseForIncorrectCredentials() {
        String auth = Base64.getEncoder().encodeToString("admin:password123".getBytes());
        assertFalse(authMiddleware.isAuthorized("Basic " + auth));
    }

    @Test
    public void returnsFalseForNoCredentials() {
        assertFalse(authMiddleware.isAuthorized(""));
    }

    @Test
    public void returnsTrueForValidCredentials() {
        String auth = Base64.getEncoder().encodeToString("admin:hunter2".getBytes());
        assertTrue(authMiddleware.isAuthorized("Basic " + auth));
    }
}
