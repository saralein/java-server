package com.saralein.server.Controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public interface Controller {
    Response createResponse(Request request);
}
