package com.saralein.server.handler;

import com.saralein.server.filesystem.File;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.mocks.MockIO;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileHandlerTest {
    private Path root;
    private MockIO mockIO;
    private FileHandler fileHandler;
    private Response.Builder responseBuilder;

    private Request requestWithMethod(String method) {
        return new Request.Builder()
                .method(method)
                .uri("/cheetara.jpg")
                .build();
    }

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        byte[] mockResponse = "File read".getBytes();
        mockIO = new MockIO(mockResponse);
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        String etag = DatatypeConverter.printHexBinary(sha1.digest(mockResponse)).toLowerCase();
        fileHandler = new FileHandler(new File(sha1), new FilePath(root), mockIO);
        responseBuilder = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "image/jpeg")
                .addHeader("ETag", etag);
    }

    @Test
    public void returnsGetResponse() throws IOException {
        Response expected = responseBuilder
                .body("File read")
                .build();
        Request request = requestWithMethod("GET");
        Response response = fileHandler.handle(request);

        assert (mockIO.readCalledWithPath(root.resolve("cheetara.jpg")));
        assert (response.equals(expected));
    }

    @Test
    public void returnsHeadResponse() throws IOException {
        Response expected = responseBuilder.build();
        Request request = requestWithMethod("HEAD");
        Response response = fileHandler.handle(request);

        assert (response.equals(expected));
    }
}
