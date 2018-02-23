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

public class FileHandlerTest {
    private MockIO mockIO;
    private FileHandler fileHandler;
    private Path root;
    private String etag;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        byte[] mockResponse = "File read".getBytes();
        mockIO = new MockIO(mockResponse);
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        etag = DatatypeConverter.printHexBinary(sha1.digest(mockResponse)).toLowerCase();
        fileHandler = new FileHandler(new File(sha1), new FilePath(root), mockIO);
    }
    
    @Test
    public void returnsGetResponse() throws IOException {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/cheetara.jpg")
                .build();
        Response response = fileHandler.handle(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 200 OK\r\nETag: " +
                etag + "\r\nContent-Type: image/jpeg\r\n\r\n";

        assertEquals(expected, header.formatToString());
        assert (mockIO.readCalledWithPath(root.resolve("cheetara.jpg")));
    }

    @Test
    public void returnsHeadResponse() throws IOException {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/cheetara.jpg")
                .build();
        Response response = fileHandler.handle(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 200 OK\r\nETag: " +
                etag + "\r\nContent-Type: image/jpeg\r\n\r\n";

        assertEquals(expected, header.formatToString());
    }
}
