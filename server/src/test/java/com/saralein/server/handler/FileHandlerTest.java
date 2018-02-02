package com.saralein.server.handler;

import com.saralein.server.FileHelper;
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

public class FileHandlerTest {
    private FileHandler fileHandler;
    private Path root;

    @Before
    public void setUp() {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        FileHelper fileHelper = new FileHelper(root);
        fileHandler = new FileHandler(fileHelper);
    }

    private byte[] getFileBytes(String file) throws IOException {
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
        Response response = fileHandler.handle(request);
        byte[] body = getFileBytes("/cheetara.jpg");
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: image/jpeg\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForGIF() throws IOException {
        Request request = createRequest("/marshmallow.gif");
        Response response = fileHandler.handle(request);
        byte[] body = getFileBytes("/marshmallow.gif");
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: image/gif\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForTXT() throws IOException {
        Request request = createRequest("/recipe.txt");
        Response response = fileHandler.handle(request);
        byte[] body = getFileBytes("/recipe.txt");
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForPDF() throws IOException {
        Request request = createRequest("/cake.pdf");
        Response response = fileHandler.handle(request);
        byte[] body = getFileBytes("/cake.pdf");
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: application/pdf\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }
}
