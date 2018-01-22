package com.saralein.server.controller;

import com.saralein.server.filesystem.ServerPaths;
import com.saralein.server.mocks.MockFilesInfo;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DirectoryControllerTest {
    private String body;
    private MockFilesInfo mockFiles;
    private Request request;
    private DirectoryController directoryController;

    @Before
    public void setUp() {
        body = "<li><a href=/cake.pdf>cake.pdf</a></li>" +
                      "<li><a href=/cheetara.jpg>cheetara.jpg</a></li>" +
                      "<li><a href=/marshmallow.gif>marshmallow.gif</a></li>" +
                      "<li><a href=/recipe.txt>recipe.txt</a></li>" +
                      "<li><a href=/sloths/>sloths/</a></li>";
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        mockFiles = new MockFilesInfo();
        request = new Request.Builder()
                .method(Methods.GET)
                .uri("/")
                .build();

        directoryController = new DirectoryController(new ServerPaths(root), mockFiles);
    }

    @Test
    public void returnsValidDirectoryResponse() {
        Response response = directoryController.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
        assertEquals(body, new String(response.getBody()));
    }

    @Test
    public void returnsServerErrorWhenDirectoryListingFails() {
        mockFiles.setToThrowError();
        Response response = directoryController.respond(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 500 Internal Server Error\r\n\r\n", header.formatToString());
    }
}