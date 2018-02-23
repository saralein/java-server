package com.saralein.server.handler;

import com.saralein.server.exchange.Header;
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
import static org.junit.Assert.assertEquals;

public class PatchHandlerTest {
    private MockIO mockIO;
    private PatchHandler patchHandler;
    private Path root;
    private String requestBody;
    private String etag;
    private String requestETag;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        requestBody = "Sliced rice cakes";
        byte[] mockResponse = "File read".getBytes();
        mockIO = new MockIO(mockResponse);
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        patchHandler = new PatchHandler(new File(sha1), new FilePath(root), mockIO);
        etag = DatatypeConverter.printHexBinary(sha1.digest(mockResponse)).toLowerCase();
        requestETag = DatatypeConverter.printHexBinary(sha1.digest(mockResponse)).toLowerCase();
    }

    @Test
    public void returns204ForSuccessfulPatch() throws IOException {
        Path path = root.resolve("recipe.txt");
        String body = "Sliced rice cakes";
        Request request = new Request.Builder()
                .method("PATCH")
                .uri("/recipe.txt")
                .addHeader("If-Match", etag)
                .body(requestBody)
                .build();
        Response response = patchHandler.handle(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 204 No Content\r\nETag: " +
                requestETag + "\r\nContent-Location: /recipe.txt\r\n\r\n";

        assertEquals(expected, header.formatToString());
        assert (mockIO.readCalledWithPath(path));
        assert (mockIO.writeCalledWith(path, body));
    }

    @Test
    public void returns409ForPatchConflict() throws IOException {
        Request request = new Request.Builder()
                .method("PATCH")
                .uri("/recipe.txt")
                .addHeader("If-Match", "abc123")
                .body(requestBody)
                .build();
        Response response = patchHandler.handle(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 409 Conflict\r\nContent-Type: text/html\r\n\r\n";

        assertEquals(expected, header.formatToString());
        assert (mockIO.readCalledWithPath(root.resolve("recipe.txt")));
    }
}
