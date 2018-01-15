package com.saralein.server.middleware;

import com.saralein.server.controller.Controller;
import com.saralein.server.mocks.MockController;
import com.saralein.server.request.Request;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StaticMiddlewareTest {
    private Controller mockController;
    private Controller application;

    @Before
    public void setUp() {
        String rootPath = System.getProperty("user.dir") + "/src/test/public";
        Path root = Paths.get(rootPath);

        Controller directoryController = new MockController(200, "Directory response");
        Controller fileController = new MockController(200, "File response");

        StaticMiddleware staticMiddleware = new StaticMiddleware(root, directoryController, fileController);
        application = staticMiddleware.use(mockController);

        mockController = new MockController(200, "Router response");
    }

    @Test
    public void returnsDirectoryResponseForDirectory() {
        Request request = new Request.Builder()
                .addMethod("GET")
                .addUri("/")
                .build();

        Response response = application.createResponse(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertArrayEquals("Directory response".getBytes(), response.getBody());
    }

    @Test
    public void returnsFileResponseForFile() {
        Request request = new Request.Builder()
                .addMethod("GET")
                .addUri("/cheetara.jpg")
                .build();

        Response response = application.createResponse(request);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 200 OK\r\n\r\n", header.formatToString());
        assertArrayEquals("File response".getBytes(), response.getBody());
    }
}