package com.saralein.cobspec.controller;

import com.saralein.server.controller.Controller;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class LogController implements Controller {
    private final String username;
    private final String password;
    private final Path log;

    public LogController(Path log, String username, String password) {
        this.username = username;
        this.password = password;
        this.log = log;
    }

    public Response createResponse(Request request) {
        return isAuthorized(request) ? createAuthorizedResponse() : createUnAuthorizedResponse();
    }

    private Response createAuthorizedResponse() {
        return new ResponseBuilder()
                    .addStatus(200)
                    .addBody(createBody())
                    .build();
    }

    private Response createUnAuthorizedResponse() {
        return new ResponseBuilder()
                .addStatus(401)
                .addHeader("WWW-Authenticate", "Basic realm=\"ServerCity\"")
                .build();
    }

    private String parseRequestAuthorization(Request request) {
        String encodedAuthorization = request.getAuthorization();
        if (encodedAuthorization.isEmpty()) {
            return null;
        } else {
            String[] encodedAuthParts = encodedAuthorization.split(" ");
            return encodedAuthParts[1];
        }
    }

    private String encodeValidAuthorization() {
        String validAuthorization = username + ":" + password;
        return Base64.getEncoder().encodeToString(validAuthorization.getBytes());
    }

    private boolean isAuthorized(Request request) {
        return request.getAuthorization() != null &&
                encodeValidAuthorization().equals(parseRequestAuthorization(request));
    }

    private byte[] createBody() {
        try {
            return Files.readAllBytes(log);
        } catch (IOException e) {
            return new byte[]{};
        }
    }
}
