package com.saralein.server.handler;

import com.saralein.server.FileHelper;
import com.saralein.server.exchange.Header;
import com.saralein.server.mocks.MockIO;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import javax.xml.bind.DatatypeConverter;
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
        FileHelper fileHelper = new FileHelper(root);
        byte[] mockResponse = "File read".getBytes();
        mockIO = new MockIO(mockResponse);
        fileHandler = new FileHandler(fileHelper, mockIO);

        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        etag = DatatypeConverter.printHexBinary(sha1.digest(mockResponse)).toLowerCase();
    }

    private Request createRequest(String uri) {
        return new Request.Builder()
                .method("GET")
                .uri(uri)
                .build();
    }

    @Test
    public void returnsResponseForJPG() throws Exception {
        Request request = createRequest("/cheetara.jpg");
        Response response = fileHandler.handle(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 200 OK\r\nContent-Type: image/jpeg\r\n\r\n";

        assertEquals(expected, header.formatToString());
        assert (mockIO.readCalledWith(root.resolve("cheetara.jpg")));
    }

    @Test
    public void returnsResponseForGIF() throws Exception {
        Request request = createRequest("/marshmallow.gif");
        Response response = fileHandler.handle(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 200 OK\r\nContent-Type: image/gif\r\n\r\n";

        assertEquals(expected, header.formatToString());
        assert (mockIO.readCalledWith(root.resolve("marshmallow.gif")));
    }

    @Test
    public void returnsResponseForTXT() throws Exception {
        Request request = createRequest("/recipe.txt");
        Response response = fileHandler.handle(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n";

        assertEquals(expected, header.formatToString());
        assert (mockIO.readCalledWith(root.resolve("recipe.txt")));
    }

    @Test
    public void returnsResponseForPDF() throws Exception {
        Request request = createRequest("/cake.pdf");
        Response response = fileHandler.handle(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 200 OK\r\nContent-Type: application/pdf\r\n\r\n";

        assertEquals(expected, header.formatToString());
        assert (mockIO.readCalledWith(root.resolve("cake.pdf")));
    }

    @Test
    public void returns204ForSuccessfulPatch() throws Exception {
        Path path = root.resolve("recipe.txt");
        String body = "Sliced rice cakes";
        Request request = new Request.Builder()
                .method("PATCH")
                .uri("/recipe.txt")
                .addHeader("If-Match", etag)
                .body(body)
                .build();
        Response response = fileHandler.handle(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 204 No Content\r\nContent-Location: /recipe.txt\r\n\r\n";

        assertEquals(expected, header.formatToString());
        assert (mockIO.readCalledWith(path));
        assert (mockIO.writeCalledWith(path, body));
    }

    @Test
    public void returns409ForPatchConflict() throws Exception {
        Request request = new Request.Builder()
                .method("PATCH")
                .uri("/recipe.txt")
                .addHeader("If-Match", "abc123")
                .body("Sliced rice cakes")
                .build();
        Response response = fileHandler.handle(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 409 Conflict\r\nContent-Type: text/html\r\n\r\n";

        assertEquals(expected, header.formatToString());
        assert (mockIO.readCalledWith(root.resolve("recipe.txt")));
    }
}
