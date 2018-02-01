package com.saralein.server.middleware;

import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class FinalCallableTest {
    @Test
    public void returns404Response() {
        FinalCallable finalCallable = new FinalCallable();
        Request request = new Request.Builder().build();
        Response response = finalCallable.call(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals("404 Not Found".getBytes(), response.getBody());
    }
}