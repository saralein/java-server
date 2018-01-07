package com.saralein.server.middleware;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
import java.util.Base64;
import java.util.List;

public class AuthMiddleware implements Middleware {
    private final String username;
    private final String password;
    private final String realm;
    private final List<String> routes;

    public AuthMiddleware(String username,
                          String password,
                          String realm,
                          List<String> routes) {
        this.username = username;
        this.password = password;
        this.realm = realm;
        this.routes = routes;
    }

    public Controller use(Controller controller) {
        return (request) -> {
            if (isAuthorized(request)) {
                return controller.createResponse(request);
            } else {
                return createResponse();
            }
        };
    }

    public Response createResponse() {
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

    private boolean noAuthRequired(Request request) {
        return !routes.contains(request.getUri());
    }

    private boolean isAuthorized(Request request) {
        return noAuthRequired(request) ||
               (request.getAuthorization() != null &&
                      encodeValidAuthorization().equals(parseRequestAuthorization(request)));
    }
}