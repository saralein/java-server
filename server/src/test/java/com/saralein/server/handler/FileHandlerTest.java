package com.saralein.server.handler;

import com.saralein.server.FileHelper;
import com.saralein.server.exchange.Header;
import com.saralein.server.mocks.MockIO;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.assertEquals;

public class FileHandlerTest {
    private MockIO mockIO;
    private FileHandler fileHandler;
    private Path root;

    @Before
    public void setUp() {
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        FileHelper fileHelper = new FileHelper(root);
        byte[] mockResponse = "File read".getBytes();
        mockIO = new MockIO(mockResponse);
        fileHandler = new FileHandler(fileHelper, mockIO);
    }
    
    @Test
    public void returnsResponseForFile() throws IOException {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/cheetara.jpg")
                .build();
        Response response = fileHandler.handle(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 200 OK\r\nContent-Type: image/jpeg\r\n\r\n";

        assertEquals(expected, header.formatToString());
        assert (mockIO.readCalledWith(root.resolve("cheetara.jpg")));
    }

    @Test
    public void returnsHead() throws IOException {
        Request request = new Request.Builder()
                .method("GET")
                .uri("/cheetara.jpg")
                .build();
        Response response = fileHandler.handle(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 200 OK\r\nContent-Type: image/jpeg\r\n\r\n";

        assertEquals(expected, header.formatToString());
    }
}
