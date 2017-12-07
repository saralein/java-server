package com.saralein.core.router;

import com.saralein.core.request.Request;
import com.saralein.core.response.Response;

public interface Router {
    Response resolveRequest(Request request);
}
