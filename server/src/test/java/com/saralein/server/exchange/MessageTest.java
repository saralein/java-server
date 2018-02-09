package com.saralein.server.exchange;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MessageTest {
    private Message message;
    private Header header;
    private byte[] body;

    @Before
    public void setUp() {
        message = new Message();
        header = new Header();
        header.status(200);
        header.addHeader("Content-Type", "text/plain");
        body = "Hello".getBytes();
    }

    @Test
    public void returnsShortMessageWithStatusLineAndHeaders() {
        assertEquals(header.formatToString(), message.summary(header));
    }

    @Test
    public void returnsFullMessageWithAllParts() {
        byte[] expected = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\nHello".getBytes();
        assertArrayEquals(expected, message.full(header, body));
    }
}
