package com.saralein.server.authorization;

import java.util.Base64;

public class Authorizer {
    private final String username;
    private final String password;

    public Authorizer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean isAuthorized(String encodedAuthorization) {
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
