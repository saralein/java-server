package com.saralein.server.controller;

import com.saralein.server.filesystem.ServerPaths;
import com.saralein.server.mocks.MockIO;
import com.saralein.server.mocks.MockFilesInfo;
import com.saralein.server.protocol.Methods;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class PartialContentControllerTest {
    private static final String RANGE_NOT_SATISFIABLE = "HTTP/1.1 416 Range Not Satisfiable\r\n" +
            "Content-Range: */22\r\n\r\n";
    private static final String SERVER_ERROR = "HTTP/1.1 500 Internal Server Error\r\n\r\n";

    private final String range;
    private final int start;
    private final int end;
    private final String expectedBody;
    private PartialContentController partialContentController;
    private MockIO mockIO;

    public PartialContentControllerTest(String range, int start, int end, String expectedBody) {
        this.range = range;
        this.start = start;
        this.end = end;
        this.expectedBody = expectedBody;
    }

    private Request createRequest(String range) {
        return new Request.Builder()
                .method(Methods.GET)
                .uri("/recipe.txt")
                .addHeader("Range", range)
                .build();
    }

    private String expectedHeader(int start, int end) {
        return "HTTP/1.1 206 Partial Content\r\n" +
                String.format("Content-Range: bytes %d-%d/22\r\n", start, end) +
                "Content-Type: text/plain\r\n\r\n";
    }

    @Before
    public void setUp() {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        ServerPaths paths = new ServerPaths(root);
        MockFilesInfo mockFiles = new MockFilesInfo();
        mockIO = new MockIO();
        mockFiles.setMimeType("text/plain");
        partialContentController = new PartialContentController(paths, mockFiles, mockIO);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {"bytes=0-21", 0, 21, "readPartialFile called"},
                {"bytes=2-16", 2, 16, "adPartialFile c"},
                {"bytes=2-", 2, 21, "adPartialFile called"},
                {"bytes=-6", 16, 21, "called"}
        });
    }

    @Test
    public void returns206HeaderForValidRanges() {
        Request request = createRequest(range);
        Response response = partialContentController.respond(request);
        Header header = response.getHeader();

        assertEquals(expectedHeader(start, end), header.formatToString());
        assertEquals(expectedBody, new String(response.getBody()));
    }

    @Test
    public void returns416ForBelowRangeRequest() {
        Request request = createRequest("bytes=-23");
        Response response = partialContentController.respond(request);
        Header header = response.getHeader();

        assertEquals(RANGE_NOT_SATISFIABLE, header.formatToString());
    }

    @Test
    public void returns416ForAboveRangeRequest() {
        Request request = createRequest("bytes=2-22");
        Response response = partialContentController.respond(request);
        Header header = response.getHeader();

        assertEquals(RANGE_NOT_SATISFIABLE, header.formatToString());
    }

    @Test
    public void returnsServerErrorIfIOFails() {
        mockIO.setToThrowError();
        Request request = createRequest("bytes=3-5");
        Response response = partialContentController.respond(request);
        Header header = response.getHeader();

        assertEquals(SERVER_ERROR, header.formatToString());
    }
}
