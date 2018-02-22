package com.saralein.server.middleware;

import com.saralein.server.authorization.Authorizer;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;

public class AuthMiddleware implements Middleware {
    private final Authorizer authorizer;
    private final String realm;
    private String route;
    private Callable next;

    public AuthMiddleware(Authorizer authorizer, String route, String realm) {
        this.authorizer = authorizer;
        this.route = route;
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
        String uri = request.getUri();
        String encodedAuth = request.getHeader("Authorization");

        if (!uri.equals(route) || authorizer.isAuthorized(encodedAuth)) {
            return next.call(request);
        }

        return unauthorized();
    }

    private Response unauthorized() {
        String basicRealm = String.format("Basic realm=\"%s\"", realm);
        return new ErrorResponse(401).respond("WWW-Authenticate", basicRealm);
    }
}
