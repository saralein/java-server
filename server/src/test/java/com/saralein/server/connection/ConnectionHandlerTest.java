package com.saralein.server.connection;

import com.saralein.server.Application;
import com.saralein.server.FileHelper;
import com.saralein.server.middleware.StaticMiddleware;
import com.saralein.server.mocks.MockController;
import com.saralein.server.mocks.MockLogger;
import com.saralein.server.mocks.MockSocket;
import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Routes;
import com.saralein.server.router.Router;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class ConnectionHandlerTest {
    private MockSocket socket;
    private ConnectionHandler connectionHandler;
    private MockController directoryController;
    private ResponseSerializer responseSerializer;

    @Before
    public void setUp() {
        Path root = Paths.get(System.getProperty("user.dir"), "src/test/public");
        MockLogger logger = new MockLogger();
        RequestParser requestParser = new RequestParser();
        responseSerializer = new ResponseSerializer();
        socket = new MockSocket();
        directoryController = new MockController(200, "Directory response");
        Router router = new Router(new Routes());
        StaticMiddleware staticMiddleware = new StaticMiddleware(
                new FileHelper(root), router, directoryController, directoryController);
        Application application = new Application(staticMiddleware);
        connectionHandler = new ConnectionHandler(socket, logger, application, requestParser, responseSerializer);
    }

    @Test
    public void handlesValidRequestFromSocket() {
        String directoryString = "GET / HTTP/1.1";
        Request request = new Request.Builder()
                .method("GET")
                .uri("/")
                .build();

        Response directoryResponse = directoryController.respond(request);
        byte[] directoryBytes = responseSerializer.convertToBytes(directoryResponse);

        socket.setRequest(directoryString);
        connectionHandler.run();

        assertArrayEquals(directoryBytes, socket.getResponseReceived());
    }

    @Test
    public void handlesInvalidRequestFromSocket() {
        String notFoundString = "GET /snarf.jpg HTTP/1.1";
        Response response = new Response.Builder()
                .status(404)
                .addHeader("Content-Type", "text/html")
                .body("404: Page not found.")
                .build();

        byte[] notFoundBytes = responseSerializer.convertToBytes(response);

        socket.setRequest(notFoundString);
        connectionHandler.run();

        assertArrayEquals(notFoundBytes, socket.getResponseReceived());
    }
}