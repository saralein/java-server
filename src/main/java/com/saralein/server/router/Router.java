package com.saralein.server.router;

import com.saralein.server.request.Request;

public interface Router {
    byte[] getResponse(Request request);
}
