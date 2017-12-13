package com.saralein.server.router;

import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public interface Router {
    Response resolveRequest(Request request);
}
