package com.saralein.server.connection;

import com.saralein.server.Application;
import com.saralein.server.FileHelper;
import com.saralein.server.middleware.DirectoryMiddleware;
import com.saralein.server.middleware.Middleware;
import com.saralein.server.mocks.MockHandler;
import com.saralein.server.mocks.MockLogger;
import com.saralein.server.mocks.MockSocket;
import com.saralein.server.request.Request;
import com.saralein.server.request.parser.*;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Router;
import com.saralein.server.router.Routes;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.assertArrayEquals;

public class ConnectionHandlerTest {
    private MockSocket socket;
    private ConnectionHandler connectionHandler;
    private MockHandler directoryHandler;
    private ResponseSerializer responseSerializer;

    @Before
    public void setUp() {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        MockLogger logger = new MockLogger();
        RequestParser requestParser = new RequestParser(
                new RequestLineParser(), new HeaderParser(),
                new ParameterParser(), new CookieParser());
        responseSerializer = new ResponseSerializer();
        socket = new MockSocket();
        directoryHandler = new MockHandler(200, "Directory response");
        Router router = new Router(new Routes());
        Middleware staticMiddleware = new DirectoryMiddleware(new FileHelper(root), directoryHandler);
        Application application = new Application(staticMiddleware.apply(router));
        connectionHandler = new ConnectionHandler(socket, logger, application, requestParser, responseSerializer);
    }

    @Test
    public void handlesValidRequestFromSocket() throws IOException {
        String directoryString = "GET / HTTP/1.1\r\n\r\n";
        Request request = new Request.Builder()
                .method("GET")
                .uri("/")
                .build();

        Response directoryResponse = directoryHandler.handle(request);
        byte[] directoryBytes = responseSerializer.convertToBytes(directoryResponse);

        socket.setRequest(directoryString);
        connectionHandler.run();

        assertArrayEquals(directoryBytes, socket.getResponseReceived());
    }

    @Test
    public void handlesInvalidRequestFromSocket() {
        String notFoundString = "GET /snarf.jpg HTTP/1.1\r\n\r\n";
        Response response = new Response.Builder()
                .status(404)
                .addHeader("Content-Type", "text/html")
                .body("404 Not Found")
                .build();

        byte[] notFoundBytes = responseSerializer.convertToBytes(response);

        socket.setRequest(notFoundString);
        connectionHandler.run();

        assertArrayEquals(notFoundBytes, socket.getResponseReceived());
    }
}
