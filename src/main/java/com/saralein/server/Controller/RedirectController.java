package com.saralein.server.Controller;

import com.saralein.server.response.ResponseBuilder;

public class RedirectController implements Controller {
    public byte[] createResponse() {
        return new ResponseBuilder()
                    .addStatus(302)
                    .addHeader("Location", "/")
                    .build()
                    .convertToBytes();
    }
}
