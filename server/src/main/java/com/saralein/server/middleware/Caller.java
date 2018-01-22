package com.saralein.server.middleware;

import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public interface Caller {
    Response call(Request request);
}
