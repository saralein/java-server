package com.saralein.server.request;

import java.util.HashMap;

import com.saralein.server.protocol.Methods;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RequestTest {
    private Request request;
    private Request requestLineOnly;

    @Before
    public void setUp() {
        request = new Request.Builder()
                .method("GET")
                .uri("/cheetara.jpg")
                .body("Hello")
                .addHeader("Authorization", "encodedCredentials")
                .build();

        requestLineOnly = new Request.Builder()
                .method("GET")
                .uri("/cheetara.jpg")
                .build();
    }

    @Test
    public void addsMethodToRequestWithString() {
        Request request = new Request.Builder()
                .method("GET")
                .build();

        assertEquals("GET", request.getMethod());
    }

    @Test
    public void addsMethodToRequestWithEnum() {
        Request request = new Request.Builder()
                .method(Methods.GET)
                .build();

        assertEquals("GET", request.getMethod());
    }

    @Test
    public void addsUriToRequest() {
        Request request = new Request.Builder()
                .uri("/")
                .build();

        assertEquals("/", request.getUri());
    }

    @Test
    public void addsBodyToRequest() {
        Request request = new Request.Builder()
                .body("Hi")
                .build();

        assertEquals("Hi", request.getBody());
    }

    @Test
    public void addsHeaderToRequest() {
        Request request = new Request.Builder()
                .addHeader("Header", "text")
                .build();

        assertEquals("text", request.getHeader("Header"));
    }

    @Test
    public void addsMapOfHeadersToRequest() {
        HashMap<String, String> headers = new HashMap<String, String>(){{
            put("Header1", "text");
            put("Header2", "more text");
        }};

        Request request = new Request.Builder()
                .addHeaders(headers)
                .build();

        assertEquals("text", request.getHeader("Header1"));
        assertEquals("more text", request.getHeader("Header2"));
    }

    @Test
    public void returnsMethodOfRequest() {
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void returnsUriOfRequest() {
        assertEquals("/cheetara.jpg", request.getUri());
    }

    @Test
    public void returnsBodyOfRequest() {
        assertEquals("Hello", request.getBody());
        assertEquals("", requestLineOnly.getBody());
    }

    @Test
    public void returnsHeader() {
        assertEquals("encodedCredentials", request.getHeader("Authorization"));
        assertEquals("", requestLineOnly.getHeader("Authorization"));
    }

    @Test
    public void returnsFullRequestLine() {
        assertEquals("GET /cheetara.jpg HTTP/1.1", request.getRequestLine());
    }
}