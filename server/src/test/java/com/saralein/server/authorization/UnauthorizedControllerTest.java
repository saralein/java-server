package com.saralein.server.authorization;

import com.saralein.server.controller.UnauthorizedController;
import com.saralein.server.exchange.Header;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnauthorizedControllerTest {
    private UnauthorizedController unauthorizedController;

    @Before
    public void setUp() {
        unauthorizedController = new UnauthorizedController("ServerCity");
    }

    @Test
    public void returns401Response() {
        Request request = new Request.Builder().build();
        Response response = unauthorizedController.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 401 Unauthorized\r\nWWW-Authenticate: " +
                "Basic realm=\"ServerCity\"\r\n\r\n", header.formatToString());
    }
}
