package com.saralein.server.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Response;

public interface Controller {
    Response respond(Request request);
}
