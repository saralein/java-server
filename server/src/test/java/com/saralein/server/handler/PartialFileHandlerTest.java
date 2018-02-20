package com.saralein.server.handler;

import com.saralein.server.filesystem.File;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.mocks.MockIO;
import com.saralein.server.range.RangeParser;
import com.saralein.server.request.Request;
import com.saralein.server.response.ErrorResponse;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PartialFileHandlerTest {
    private Path root;
    private String etag;
    private MockIO mockIO;
    private PartialFileHandler partialFileHandler;

    private Request requestWithRange(String range) {
        return new Request.Builder()
                .method("GET")
                .uri("/recipe.txt")
                .addHeader("Range", range)
                .build();
    }

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        byte[] mockResponse = "1 cup rice".getBytes();
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        etag = DatatypeConverter.printHexBinary(sha1.digest(mockResponse)).toLowerCase();
        mockIO = new MockIO(mockResponse);
        partialFileHandler = new PartialFileHandler(
                new File(sha1), new FilePath(root), mockIO, new RangeParser());
    }

    @Test
    public void returns206HeaderForValidRanges() throws IOException {
        Response expected = new Response.Builder()
                .status(206)
                .addHeader("Content-Range", "bytes 2-6/10")
                .addHeader("Content-Type", "text/plain")
                .addHeader("ETag", etag)
                .body("cup r")
                .build();
        Response response = partialFileHandler.handle(requestWithRange("bytes=2-6"));

        assert (response.equals(expected));
        assert (mockIO.readCalledWithPath(root.resolve("recipe.txt")));
    }

    @Test
    public void returns416InvalidRangeRequest() throws IOException {
        Response expected = new ErrorResponse(416)
                .respond("Content-Range", "*/10");
        Response response = partialFileHandler.handle(requestWithRange("bytes=-11"));

        assert (response.equals(expected));
    }
}
