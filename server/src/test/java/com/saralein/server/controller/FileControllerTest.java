package com.saralein.server.controller;

import com.saralein.server.filesystem.ServerPaths;
import com.saralein.server.mocks.MockIO;
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

public class FileControllerTest {
    private Request getRequest;
    private MockIO mockIO;
    private FileController fileController;

    @Before
    public void setUp() {
        getRequest = new Request.Builder()
                .method(Methods.GET)
                .uri("/cheetara.jpg")
                .build();
        Path root = Paths.get( System.getProperty("user.dir"), "src/test/public");
        MockFilesInfo mockFiles = new MockFilesInfo();
        mockIO = new MockIO();
        fileController = new FileController(new ServerPaths(root), mockFiles, mockIO);
    }

    @Test
    public void returnsHeadResponse() {
        Request request = new Request.Builder()
                .method(Methods.HEAD)
                .uri("/cheetara.jpg")
                .build();
        Response response = fileController.respond(request);
        Header header = response.getHeader();
        String expectedHead = "HTTP/1.1 200 OK\r\nAccept-Ranges: bytes\r\nContent-Type: image/jpeg\r\n\r\n";

        assertEquals(expectedHead, header.formatToString());
    }

    @Test
    public void returnsGetResponse() {
        Response response = fileController.respond(getRequest);
        Header header = response.getHeader();
        String expectedHead = "HTTP/1.1 200 OK\r\nContent-Type: image/jpeg\r\n\r\n";

        assertEquals(expectedHead, header.formatToString());
        assertArrayEquals("readFullFile called".getBytes(), response.getBody());
    }

    @Test
    public void returnsServerErrorResponseIfFileReadFails() {
        mockIO.setToThrowError();
        Response response = fileController.respond(getRequest);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 500 Internal Server Error\r\n\r\n", header.formatToString());
    }
}
