package com.saralein.server.authorization;

import com.saralein.server.request.Request;
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
        Request request = new Request.Builder()
                .addHeader("Authorization", "Basic " + auth)
                .build();
        assertFalse(authorizer.isAuthorized(request));
    }

    @Test
    public void returnsFalseForNoCredentials() {
        Request request = new Request.Builder()
                .addHeader("Authorization", "")
                .build();
        assertFalse(authorizer.isAuthorized(request));
    }

    @Test
    public void returnsTrueForValidCredentials() {
        String auth = Base64.getEncoder().encodeToString("admin:hunter2".getBytes());
        Request request = new Request.Builder()
                .addHeader("Authorization", "Basic " + auth)
                .build();
        assertTrue(authorizer.isAuthorized(request));
    }
}
