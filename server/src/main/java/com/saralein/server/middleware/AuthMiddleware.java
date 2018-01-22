package com.saralein.server.middleware;

import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.router.RouteConfig;
import com.saralein.server.router.Routes;
import java.util.Base64;

public class AuthMiddleware extends Middleware {
    private final String realm;
    private final Routes routes;

    public AuthMiddleware(Routes routes, String realm) {
        super();
        this.realm = realm;
        this.routes = routes;
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
        return new Response.Builder()
                .status(401)
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

    private String encodeValidAuthorization(String username, String password) {
        String validAuthorization = username + ":" + password;
        return Base64.getEncoder().encodeToString(validAuthorization.getBytes());
    }

    private boolean isAuthorized(Request request) {
        return !requiresAuth(request) ||
                hasValidCredentials(request);
    }

    private boolean requiresAuth(Request request) {
        RouteConfig configuration = getConfiguration(request);
        return configuration.getValue("username") != null && configuration.getValue("password") != null;
    }

    private boolean hasValidCredentials(Request request) {
        RouteConfig configuration = getConfiguration(request);
        String username = configuration.getValue("username");
        String password = configuration.getValue("password");
        return encodeValidAuthorization(username, password).equals(parseRequestAuthorization(request));
    }

    private RouteConfig getConfiguration(Request request) {
        return routes.getConfig(request.getUri());
    }
}
