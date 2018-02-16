package com.saralein.server.middleware.verifier;

import com.saralein.server.request.Request;

public interface Verifier {
    boolean validForMiddleware(Request request);
}
