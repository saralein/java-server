package com.saralein.server.controller;

import com.saralein.server.FileHelper;
import com.saralein.server.mocks.MockFileIO;
import com.saralein.server.partial_content.RangeParser;
import com.saralein.server.partial_content.RangeValidator;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class PartialContentControllerTest {
    private final String range;
    private final int start;
    private final int end;
    private PartialContentController partialContentController;

    public PartialContentControllerTest(String range, int start, int end) {
        this.range = range;
        this.start = start;
        this.end = end;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {"bytes=0-9", 0, 9},
                {"bytes=2-6", 2, 6},
                {"bytes=2-", 2, 9},
                {"bytes=-6", 4, 9}
        });
    }

    private Request createRequest(String range) {
        return new Request.Builder()
                .method("GET")
                .uri("/recipe.txt")
                .addHeader("Range", range)
                .build();
    }

    private String expectedHeader(int start, int end) {
        return "HTTP/1.1 206 Partial Content\r\n" +
                String.format("Content-Range: bytes %d-%d/10\r\n", start, end) +
                "Content-Type: text/plain\r\n\r\n";
    }

    @Before
    public void setUp() {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        FileHelper fileHelper = new FileHelper(root);
        partialContentController = new PartialContentController(
                fileHelper, new MockFileIO(), new RangeValidator(), new RangeParser(), new ErrorController());
    }

    @Test
    public void returns206HeaderForValidRanges() {
        Request request = createRequest(range);
        Response response = partialContentController.respond(request);
        Header header = response.getHeader();

        assertEquals(expectedHeader(start, end), header.formatToString());
        assertEquals("IO called", new String(response.getBody()));
    }

    @Test
    public void returns416ForBelowRangeRequest() {
        Request request = createRequest("bytes=-11");
        Response response = partialContentController.respond(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 416 Range Not Satisfiable\r\nContent-Range: */10\r\n\r\n";

        assertEquals(expected, header.formatToString());
    }

    @Test
    public void returns416ForAboveRangeRequest() {
        Request request = createRequest("bytes=2-11");
        Response response = partialContentController.respond(request);
        Header header = response.getHeader();
        String expected = "HTTP/1.1 416 Range Not Satisfiable\r\nContent-Range: */10\r\n\r\n";

        assertEquals(expected, header.formatToString());
    }
}