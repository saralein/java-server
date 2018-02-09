package com.saralein.server.handler;

import com.saralein.server.FileHelper;
import com.saralein.server.exchange.Header;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DirectoryHandlerTest {
    private byte[] bodyArray;
    private DirectoryHandler directoryHandler;
    private Request request;

    @Before
    public void setUp() {
        String body = "<li><a href=/cake.pdf>cake.pdf</a></li>" +
                "<li><a href=/cheetara.jpg>cheetara.jpg</a></li>" +
                "<li><a href=/marshmallow.gif>marshmallow.gif</a></li>" +
                "<li><a href=/recipe.txt>recipe.txt</a></li>" +
                "<li><a href=/sloths/>sloths/</a></li>";

        bodyArray = body.getBytes();

        String rootPath = System.getProperty("user.dir") + "/src/test/public";
        Path root = Paths.get(rootPath);
        FileHelper fileHelper = new FileHelper(root);
        request = new Request.Builder()
                .method("GET")
                .uri("/")
                .build();

        directoryHandler = new DirectoryHandler(fileHelper);
    }

    @Test
    public void returnsValidDirectoryResponse() throws IOException {
        Response response = directoryHandler.handle(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertArrayEquals(bodyArray, response.getBody());
    }
}
