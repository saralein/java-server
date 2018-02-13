package com.saralein.server.handler;

import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import java.io.IOException;

public interface Handler {
    Response handle(Request request) throws IOException;
}
