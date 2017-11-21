package com.saralein.server.controller;

import com.saralein.server.Controller.FileController;
import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.Header;
import com.saralein.server.response.SysFileHelper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
        File root = new File(rootPath);
        fileHelper = new SysFileHelper(root);
    }

    private byte[] getFileBytes(String filePath) {
        byte[] fileByte = null;

        try {
            File file = new File(filePath);
            fileByte = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            fail("Failed to createContents file bytes in test.");
        }

        return fileByte;
    }

    private byte[] getFullResponse(byte[] headers, byte[] fileBytes) {
        byte[] combined = new byte[headers.length + fileBytes.length];

        for (int i = 0; i < combined.length; ++i)
        {
            combined[i] = i < headers.length ? headers[i] : fileBytes[i - headers.length];
        }

        return combined;
    }

    private Pair<byte[], FileController> createController(String requestLine, String path, String mimeType) {
        Header header = new Header();
        header.addStatus(200);
        header.addHeader("Content-Type", mimeType);

        byte[] fileBytes = getFileBytes(path);
        File file = new File(path);
        Request request = requestParser.parse(requestLine);

        return new Pair<>(
            getFullResponse(header.convertToBytes(), fileBytes),
            new FileController(request, file, fileHelper)
        );
    }

    @Test
    public void returnsResponseForJPG() {
        Pair<byte[], FileController> jpgPair = createController(
                "GET /cheetara.jpg HTTP/1.1",
                "public/cheetara.jpg",
                "image/jpeg");
        byte[] response = jpgPair.getKey();
        FileController controller = jpgPair.getValue();

        assertArrayEquals(response, controller.createResponse());
    }

    @Test
    public void returnsResponseForGIF() {
        Pair<byte[], FileController> gifPair = createController(
                "GET /marshmallow.gif HTTP/1.1",
                "public/marshmallow.gif",
                "image/gif");
        byte[] response = gifPair.getKey();
        FileController controller = gifPair.getValue();

        assertArrayEquals(response, controller.createResponse());
    }

    @Test
    public void returnsResponseForTXT() {
        Pair<byte[], FileController> txtPair = createController(
                "GET /recipe.txt HTTP/1.1",
                "public/recipe.txt",
                "text/plain");
        byte[] response = txtPair.getKey();
        FileController controller = txtPair.getValue();

        assertArrayEquals(response, controller.createResponse());
    }

    @Test
    public void returnsResponseForPDF() {
        Pair<byte[], FileController> pdfPair = createController(
                "GET /cake.pdf HTTP/1.1",
                "public/cake.pdf",
                "application/pdf");
        byte[] response = pdfPair.getKey();
        FileController controller = pdfPair.getValue();

        assertArrayEquals(response, controller.createResponse());
    }
}