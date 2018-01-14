package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.util.Base64;

public class AuthController implements Controller {
    private final String username;
    private final String password;
    private final String realm;
    private final Controller controller;

    public AuthController(String username, String password,
                          String realm, Controller controller) {
        this.username = username;
        this.password = password;
        this.realm = realm;
        this.controller = controller;
    }

    public Response createResponse(Request request) {
        if (isAuthorized(request)) {
            return controller.createResponse(request);
        } else {
            return unauthorized();
        }
    }

    private Response unauthorized() {
        String serverRealm = String.format("Basic realm=\"%s\"", realm);
        return new Response.Builder()
                .addStatus(401)
                .addHeader("WWW-Authenticate", serverRealm)
                .build();
    }

    private String parseRequestAuthorization(Request request) {
        String encodedAuthorization = request.getHeader("Authorization");
        if (encodedAuthorization.isEmpty()) {
            return null;
        } else {
            String[] encodedAuthParts = encodedAuthorization.split(" ");
            return encodedAuthParts[1];
        }
    }

    private String encodeValidAuthorization() {
        String validAuthorization = username + ":" + password;
        return Base64.getEncoder().encodeToString(validAuthorization.getBytes());
    }

    private boolean isAuthorized(Request request) {
        return (request.getHeader("Authorization") != null &&
                        encodeValidAuthorization().equals(parseRequestAuthorization(request)));
    }
}