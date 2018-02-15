package com.saralein.server.handler;

import com.saralein.server.exchange.Header;
import com.saralein.server.filesystem.File;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.mocks.MockIO;
import com.saralein.server.range.RangeParser;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static org.junit.Assert.assertEquals;

public class PartialFileHandlerTest {
    private Path root;
    private MockIO mockIO;
    private PartialFileHandler partialFileHandler;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        mockIO = new MockIO("Partial response".getBytes());
        partialFileHandler = new PartialFileHandler(
                new File(sha1), new FilePath(root), mockIO, new RangeParser());
    }

    @Test
    public void returns206HeaderForValidRanges() throws IOException {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/recipe.txt")
                .addHeader("Range", "bytes=2-6")
                .build();
        Response response = partialFileHandler.handle(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 206 Partial Content\r\n" +
                "Content-Range: bytes 2-6/10\r\nContent-Type: text/plain\r\n\r\n";

        assertEquals(expected, header.formatToString());
        assert (mockIO.readCalledWithPath(root.resolve("recipe.txt")));
        assert (mockIO.readCalledWithStart(2));
        assert (mockIO.readCalledWithEnd(6));
    }

    @Test
    public void returns416InvalidRangeRequest() throws IOException {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/recipe.txt")
                .addHeader("Range", "bytes=-11")
                .build();
        Response response = partialFileHandler.handle(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 416 Range Not Satisfiable\r\n" +
                "Content-Range: */10\r\nContent-Type: text/html\r\n\r\n";

        assertEquals(expected, header.formatToString());
    }
}
