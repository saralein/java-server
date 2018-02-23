package com.saralein.server.callable;

import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public interface Callable {
    Response call(Request request);
}
