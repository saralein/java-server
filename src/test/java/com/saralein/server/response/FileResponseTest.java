package com.saralein.server.response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FileResponseTest {
    private byte[] jpgResponse;
    private FileResponse jpgResponder;

    private byte[] gifResponse;
    private FileResponse gifResponder;

    private byte[] txtResponse;
    private FileResponse txtResponder;

    private byte[] pdfResponse;
    private FileResponse pdfResponder;

    @Before
    public void setUp() {
        RequestParser requestParser = new RequestParser();
        SysFileHelper fileHelper = new SysFileHelper("public");

        String jpgString = "GET /cheetara.jpg HTTP/1.1";
        String jpgPath = "public/cheetara.jpg";
        String jpgHeader = new Header("200 OK", "image/jpeg").createContents();

        byte[] jpgBytes = getFileBytes(jpgPath);
        File jpgFile = new File(jpgPath);
        Request jpgRequest = requestParser.parse(jpgString);

        jpgResponse = getFullResponse(jpgHeader, jpgBytes);
        jpgResponder = new FileResponse(jpgRequest, jpgFile, fileHelper);

        String gifString = "GET /marshmallow.gif HTTP/1.1";
        String gifPath = "public/marshmallow.gif";
        String gifHeader = new Header("200 OK", "image/gif").createContents();

        byte[] gifBytes = getFileBytes(gifPath);
        File gifFile = new File(gifPath);
        Request gifRequest = requestParser.parse(gifString);

        gifResponse = getFullResponse(gifHeader, gifBytes);
        gifResponder = new FileResponse(gifRequest, gifFile, fileHelper);

        String txtString = "GET /recipe.txt HTTP/1.1";
        String txtPath = "public/recipe.txt";
        String txtHeader = new Header("200 OK", "text/plain").createContents();

        byte[] txtBytes = getFileBytes(txtPath);
        File txtFile = new File(txtPath);
        Request txtRequest = requestParser.parse(txtString);

        txtResponse = getFullResponse(txtHeader, txtBytes);
        txtResponder = new FileResponse(txtRequest, txtFile, fileHelper);

        String pdfString = "GET /cake.pdf HTTP/1.1";
        String pdfPath = "public/recipe.txt";
        String pdfHeader = new Header("200 OK", "application/pdf").createContents();

        byte[] pdfBytes = getFileBytes(pdfPath);
        File pdfFile = new File(pdfPath);
        Request pdfRequest = requestParser.parse(pdfString);

        pdfResponse = getFullResponse(pdfHeader, pdfBytes);
        pdfResponder = new FileResponse(pdfRequest, pdfFile, fileHelper);
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

    private byte[] getFullResponse(String header, byte[] fileBytes) {
        byte[] headers = header.getBytes();
        byte[] combined = new byte[headers.length + fileBytes.length];

        for (int i = 0; i < combined.length; ++i)
        {
            combined[i] = i < headers.length ? headers[i] : fileBytes[i - headers.length];
        }

        return combined;
    }

    @Test
    public void returnsResponseForJPG() {
        assertArrayEquals(jpgResponse, jpgResponder.createResponse());
    }

    @Test
    public void returnsResponseForGIF() {
        assertArrayEquals(gifResponse, gifResponder.createResponse());
    }

    @Test
    public void returnsResponseForTXT() {
        assertArrayEquals(txtResponse, txtResponder.createResponse());
    }

    @Test
    public void returnsResponseForPDF() {
        assertArrayEquals(pdfResponse, pdfResponder.createResponse());
    }
}