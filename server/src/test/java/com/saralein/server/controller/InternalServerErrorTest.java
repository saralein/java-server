package com.saralein.server.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Test;
import static org.junit.Assert.*;

public class InternalServerErrorTest {
    @Test
    public void returns500Response() {
      Request request = new Request.Builder().build();
      Response response = new InternalServerError().respond(request);
      Header header = response.getHeader();

      assertEquals("HTTP/1.1 500 Internal Server Error\r\n\r\n", header.formatToString());
    }
}