package com.saralein.server.protocol;

public enum StatusCodes {
    OK(200, "200 OK"),
    FOUND(302, "302 Found"),
    BAD_REQUEST(400, "400 Bad Request"),
    NOT_FOUND(404, "404 Not Found"),
    NOT_ALLOWED(405, "405 Method Not Allowed");

    private final int code;
    private final String status;

    StatusCodes(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public static String retrieve(int code) {
        String status = "";

        for (StatusCodes statusCode: values()) {
            if (statusCode.code == code) {
                status = statusCode.status;
            }
        }

        return status;
    }
}
