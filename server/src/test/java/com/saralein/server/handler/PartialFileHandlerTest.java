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
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class PartialFileHandlerTest {
    private String invalidRange;
    private Path root;
    private MockIO mockIO;
    private PartialFileHandler partialFileHandler;

    @Before
    public void setUp() {
        invalidRange = "HTTP/1.1 416 Range Not Satisfiable\r\n" +
                "Content-Range: */10\r\nContent-Type: text/html\r\n\r\n";
        root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        mockIO = new MockIO("Partial response".getBytes());
        partialFileHandler = new PartialFileHandler(new FileHelper(root), mockIO);
    }

    private Request createRequest(Integer start, Integer end) {
        Map<String, Integer> range = new HashMap<>();
        range.put("start", start);
        range.put("end", end);

        return new Request.Builder()
                .method("GET")
                .uri("/recipe.txt")
                .range(range)
                .build();
    }

    @Test
    public void returns206HeaderForValidRanges() throws IOException {
        int start = 2;
        int end = 8;
        Request request = createRequest(start, end);
        Response response = partialFileHandler.handle(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 206 Partial Content\r\n" +
                "Content-Range: bytes 2-8/10\r\nContent-Type: text/plain\r\n\r\n";

        assertEquals(expected, header.formatToString());
        assert (mockIO.readCalledWithPath(root.resolve("recipe.txt")));
        assert (mockIO.readCalledWithStart(start));
        assert (mockIO.readCalledWithEnd(end));
    }

    @Test
    public void returns416ForBelowRangeRequest() throws IOException {
        Request request = createRequest(null, 11);
        Response response = partialFileHandler.handle(request);
        Header header = response.getHeader();

        assertEquals(invalidRange, header.formatToString());
    }

    @Test
    public void returns416ForAboveRangeRequest() throws IOException {
        Request request = createRequest(2, 11);
        Response response = partialFileHandler.handle(request);
        Header header = response.getHeader();

        assertEquals(invalidRange, header.formatToString());
    }
}
