package com.saralein.server.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import com.saralein.server.FileHelper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileControllerTest {
    private FileHelper fileHelper;

    @Before
    public void setUp() {
        String rootPath = System.getProperty("user.dir") + "/src/test/public";
        Path root = Paths.get(rootPath);
        fileHelper = new FileHelper(root);
    }

    private byte[] getFileBytes(String filePath) throws IOException {
        Path file = Paths.get(filePath);
        return Files.readAllBytes(file);
    }

    private Request createRequest(String uri) {
        return new Request.Builder()
                .method("GET")
                .uri(uri)
                .build();
    }

    private Response createResponse(Request request, String mimeType) {
        Header header = new Header();
        header.status(200);
        header.addHeader("Content-Type", mimeType);

        return new FileController(fileHelper).respond(request);
    }

    @Test
    public void returnsResponseForJPG() throws IOException {
        Request request = createRequest("/cheetara.jpg");
        Response response = createResponse(request, "image/jpeg");
        byte[] body = getFileBytes("src/test/public/cheetara.jpg");
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: image/jpeg\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForGIF() throws IOException {
        Request request = createRequest("/marshmallow.gif");
        Response response = createResponse(request, "image/gif");
        byte[] body = getFileBytes("src/test/public/marshmallow.gif");
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: image/gif\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForTXT() throws IOException {
        Request request = createRequest("/recipe.txt");
        Response response = createResponse(request, "text/plain");
        byte[] body = getFileBytes("src/test/public/recipe.txt");
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForPDF() throws IOException {
        Request request = createRequest("/cake.pdf");
        Response response = createResponse(request, "application/pdf");
        byte[] body = getFileBytes("src/test/public/cake.pdf");
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: application/pdf\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }
}