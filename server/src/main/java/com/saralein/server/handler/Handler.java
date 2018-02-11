package com.saralein.server.handler;

import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public interface Handler {
    Response handle(Request request) throws Exception;
}
