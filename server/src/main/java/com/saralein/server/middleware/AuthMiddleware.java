package com.saralein.server.middleware;

import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
import java.util.Base64;

public class AuthMiddleware extends Middleware {
    private final String username;
    private final String password;
    private final String realm;

    public AuthMiddleware(String username, String password, String realm) {
        super();
        this.username = username;
        this.password = password;
        this.realm = realm;
    }

    @Override
    public Response call(Request request) {
        if (isAuthorized(request)) {
            return middleware.call(request);
        } else {
            return unauthorized();
        }
    }

    private Response unauthorized() {
        String serverRealm = String.format("Basic realm=\"%s\"", realm);
        return new ResponseBuilder()
                .addStatus(401)
                .addHeader("WWW-Authenticate", serverRealm)
                .build();
    }

    private String parseRequestAuthorization(Request request) {
        String encodedAuthorization = request.getAuthorization();
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
        return !request.getUri().equals("/logs") ||
                encodeValidAuthorization().equals(parseRequestAuthorization(request));
    }
}