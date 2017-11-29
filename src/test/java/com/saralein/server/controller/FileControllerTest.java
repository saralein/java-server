package com.saralein.server.controller;

import com.saralein.server.Controller.FileController;
import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import com.saralein.server.response.SysFileHelper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.util.Pair;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FileControllerTest {
    private RequestParser requestParser;
    private SysFileHelper fileHelper;

    @Before
    public void setUp() {
        requestParser = new RequestParser();
        String rootPath = System.getProperty("user.dir") + "/" + "public";
        Path root = Paths.get(rootPath);
        fileHelper = new SysFileHelper(root);
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

    private Pair<byte[], Response> createResponse(String requestLine, String path, String mimeType) {
        Header header = new Header();
        header.addStatus(200);
        header.addHeader("Content-Type", mimeType);

        Path resource = Paths.get(path);
        Request request = requestParser.parse(requestLine);

        return new Pair<>(
            getFileBytes(path),
            new FileController(request, resource, fileHelper).createResponse()
        );
    }

    @Test
    public void returnsResponseForJPG() {
        Pair<byte[], Response> jpgPair = createResponse(
                "GET /cheetara.jpg HTTP/1.1",
                "public/cheetara.jpg",
                "image/jpeg");
        byte[] body = jpgPair.getKey();
        Response response = jpgPair.getValue();
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: image/jpeg\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForGIF() {
        Pair<byte[], Response> gifPair = createResponse(
                "GET /marshmallow.gif HTTP/1.1",
                "public/marshmallow.gif",
                "image/gif");
        byte[] body = gifPair.getKey();
        Response response = gifPair.getValue();
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: image/gif\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForTXT() {
        Pair<byte[], Response> txtPair = createResponse(
                "GET /recipe.txt HTTP/1.1",
                "public/recipe.txt",
                "text/plain");
        byte[] body = txtPair.getKey();
        Response response = txtPair.getValue();
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }

    @Test
    public void returnsResponseForPDF() {
        Pair<byte[], Response> pdfPair = createResponse(
                "GET /cake.pdf HTTP/1.1",
                "public/cake.pdf",
                "application/pdf");
        byte[] body = pdfPair.getKey();
        Response response = pdfPair.getValue();
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: application/pdf\r\n\r\n", header.formatToString());
        assertArrayEquals(body, response.getBody());
    }
}