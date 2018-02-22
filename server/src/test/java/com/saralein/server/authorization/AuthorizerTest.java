package com.saralein.server.authorization;

import org.junit.Before;
import org.junit.Test;
import java.util.Base64;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AuthorizerTest {
    private Authorizer authorizer;

    @Before
    public void setUp() {
        authorizer = new Authorizer("admin", "hunter2");
    }

    @Test
    public void returnsFalseForIncorrectCredentials() {
        String auth = Base64.getEncoder().encodeToString("admin:password123".getBytes());
        assertFalse(authorizer.isAuthorized("Basic " + auth));
    }

    @Test
    public void returnsFalseForNoCredentials() {
        assertFalse(authorizer.isAuthorized(""));
    }

    @Test
    public void returnsTrueForValidCredentials() {
        String auth = Base64.getEncoder().encodeToString("admin:hunter2".getBytes());
        assertTrue(authorizer.isAuthorized("Basic " + auth));
    }
}
