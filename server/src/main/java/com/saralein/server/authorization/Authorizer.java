package com.saralein.server.authorization;

import com.saralein.server.request.Request;
import java.util.Base64;

public class Authorizer {
    private final String username;
    private final String password;

    public Authorizer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean isAuthorized(Request request) {
        String encodedAuthorization = request.getHeader("Authorization");
        return encodeValidAuthorization().equals(parseRequestAuthorization(encodedAuthorization));
    }

    private String parseRequestAuthorization(String encodedAuthorization) {
        if (encodedAuthorization.isEmpty()) {
            return null;
        }

        return encodedAuthorization.split(" ")[1];
    }

    private String encodeValidAuthorization() {
        String validAuthorization = username + ":" + password;
        return Base64.getEncoder().encodeToString(validAuthorization.getBytes());
    }
}
