package com.saralein.server.controller;

import com.saralein.server.FileHelper;
import com.saralein.server.mocks.MockController;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class FileControllerTest {
    private FileController fileController;
    private Path root;

    @Before
    public void setUp() {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        FileHelper fileHelper = new FileHelper(root);
        MockController mockController = new MockController(200, "Partial response");
        fileController = new FileController(fileHelper, mockController);
    }

    private byte[]
    getFileBytes(String file) throws IOException {
        Path filePath = root.resolve(file.substring(1));
        return Files.readAllBytes(filePath);
    }

    private Request createRequest(String uri) {
        return new Request.Builder()
                .method("GET")
                .uri(uri)
                .build();
    }

    @Test
    public void returnsResponseForJPG() throws IOException {
        Request request = createRequest("/cheetara.jpg");
        Response response = fileController.respond(request);
        byte[] body = getFileBytes("/cheetara.jpg");
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: image/jpeg\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForGIF() throws IOException {
        Request request = createRequest("/marshmallow.gif");
        Response response = fileController.respond(request);
        byte[] body = getFileBytes("/marshmallow.gif");
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: image/gif\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForTXT() throws IOException {
        Request request = createRequest("/recipe.txt");
        Response response = fileController.respond(request);
        byte[] body = getFileBytes("/recipe.txt");
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForPDF() throws IOException {
        Request request = createRequest("/cake.pdf");
        Response response = fileController.respond(request);
        byte[] body = getFileBytes("/cake.pdf");
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: application/pdf\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForPartialContent() {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/recipe.txt")
                .addHeader("Range", "bytes=0-2")
                .build();
        Response response = fileController.respond(request);

        assertArrayEquals("Partial response".getBytes(), response.getBody());
    }
}
