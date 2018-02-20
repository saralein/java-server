package com.saralein.server.handler;

import com.saralein.server.filesystem.File;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.mocks.MockIO;
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

public class PatchHandlerTest {
    private MockIO mockIO;
    private PatchHandler patchHandler;
    private Path root;
    private String etag;

    private Request requestWithETag(String etag) {
        return new Request.Builder()
                .method("PATCH")
                .uri("/recipe.txt")
                .addHeader("If-Match", etag)
                .body("Sliced rice cakes")
                .build();
    }

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        byte[] mockResponse = "File read".getBytes();
        mockIO = new MockIO(mockResponse);
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        patchHandler = new PatchHandler(new File(sha1), new FilePath(root), mockIO);
        etag = DatatypeConverter.printHexBinary(sha1.digest(mockResponse)).toLowerCase();
    }

    @Test
    public void returns204ForSuccessfulPatch() throws IOException {
        Path path = root.resolve("recipe.txt");
        String body = "Sliced rice cakes";
        Response expected = new Response.Builder()
                .status(204)
                .addHeader("Content-Location", "/recipe.txt")
                .addHeader("ETag", etag)
                .build();
        Request request = requestWithETag(etag);
        Response response = patchHandler.handle(request);

        assert (response.equals(expected));
        assert (mockIO.readCalledWithPath(path));
        assert (mockIO.writeCalledWith(path, body));
    }

    @Test
    public void returns409ForPatchConflict() throws IOException {
        Response expected = new ErrorResponse(409).respond();
        Request request = requestWithETag("abc123");
        Response response = patchHandler.handle(request);

        assert (response.equals(expected));
        assert (mockIO.readCalledWithPath(root.resolve("recipe.txt")));
    }
}
