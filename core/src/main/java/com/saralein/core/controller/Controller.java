package com.saralein.core.controller;

import com.saralein.core.request.Request;
import com.saralein.core.response.Response;

public interface Controller {
    Response createResponse(Request request);
}
