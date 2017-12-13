package com.saralein.cobspec.controller;

import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import com.saralein.cobspec.FileHelper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import javafx.util.Pair;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FileControllerTest {
    private FileHelper fileHelper;

    @Before
    public void setUp() {
        String rootPath = System.getProperty("user.dir") + "/src/test/public";
        Path root = Paths.get(rootPath);
        fileHelper = new FileHelper(root);
    }

    private byte[] getFileBytes(String filePath) {
        byte[] fileByte = null;

        try {
            Path file = Paths.get(filePath);
            fileByte = Files.readAllBytes(file);
        } catch (IOException e) {
            fail("Failed to createContents file bytes in test.");
        }

        return fileByte;
    }

    private Pair<byte[], Response> createResponse(Request request, String mimeType) {
        Header header = new Header();
        header.addStatus(200);
        header.addHeader("Content-Type", mimeType);

        return new Pair<>(
            getFileBytes("src/test/public" + request.getUri()),
            new FileController(fileHelper).createResponse(request)
        );
    }

    @Test
    public void returnsResponseForJPG() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/cheetara.jpg");
            put("version", "HTTP/1.1");
        }});

        Pair<byte[], Response> jpgPair = createResponse(request, "image/jpeg");
        byte[] body = jpgPair.getKey();
        Response response = jpgPair.getValue();
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: image/jpeg\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForGIF() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/marshmallow.gif");
            put("version", "HTTP/1.1");
        }});


        Pair<byte[], Response> gifPair = createResponse(request, "image/gif");
        byte[] body = gifPair.getKey();
        Response response = gifPair.getValue();
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: image/gif\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForTXT() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/recipe.txt");
            put("version", "HTTP/1.1");
        }});

        Pair<byte[], Response> txtPair = createResponse(request, "text/plain");
        byte[] body = txtPair.getKey();
        Response response = txtPair.getValue();
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForPDF() {
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/cake.pdf");
            put("version", "HTTP/1.1");
        }});

        Pair<byte[], Response> pdfPair = createResponse(request, "application/pdf");
        byte[] body = pdfPair.getKey();
        Response response = pdfPair.getValue();
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: application/pdf\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }
}