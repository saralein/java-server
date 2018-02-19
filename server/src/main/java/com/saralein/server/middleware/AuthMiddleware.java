package com.saralein.server.middleware;

import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import com.saralein.server.router.Routes;
import java.util.Base64;

public class AuthMiddleware implements Middleware {
    private final Routes routes;
    private final String username;
    private final String password;
    private final String realm;
    private Callable next;

    public AuthMiddleware(Routes routes, String username, String password, String realm) {
        this.routes = routes;
        this.username = username;
        this.password = password;
        this.realm = realm;
        this.next = null;
    }

    @Override
    public Middleware apply(Callable callable) {
        next = callable;
        return this;
    }

    @Override
    public Response call(Request request) {
        if (isAuthorized(request)) {
            return next.call(request);
        }

        return unauthorized();
    }

    private boolean isAuthorized(Request request) {
        String uri = request.getUri();
        String encodedAuthorization = request.getHeader("Authorization");
        String parsedAuthorization = parseRequestAuthorization(encodedAuthorization);
        return !routes.requiresAuthorization(uri) || encodeValidAuthorization().equals(parsedAuthorization);
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

    private Response unauthorized() {
        String basicRealm = String.format("Basic realm=\"%s\"", realm);
        return new ErrorResponse(401).respond("WWW-Authenticate", basicRealm);
    }
}
