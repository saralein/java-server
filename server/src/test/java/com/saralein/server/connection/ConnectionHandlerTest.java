package com.saralein.server.connection;

import com.saralein.server.controller.Controller;
import com.saralein.server.middleware.Middleware;
import com.saralein.server.middleware.StaticMiddleware;
import com.saralein.server.mocks.MockController;
import com.saralein.server.mocks.MockLogger;
import com.saralein.server.mocks.MockSocket;
import com.saralein.server.protocol.StatusCodes;
import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseBuilder;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Routes;
import com.saralein.server.router.Router;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.Test;

public class ConnectionHandlerTest {
    private MockSocket socket;
    private ConnectionHandler connectionHandler;
    private MockController directoryController;
    private ResponseSerializer responseSerializer;

    @Before
    public void setUp() {
        String rootPath = System.getProperty("user.dir") + "/src/test/public";
        Path root = Paths.get(rootPath);
        MockLogger logger = new MockLogger();
        RequestParser requestParser = new RequestParser();
        responseSerializer = new ResponseSerializer();
        socket = new MockSocket();
        directoryController = new MockController(200, "Directory response");
        Routes routes = new Routes();
        Router router = new Router(routes);
        Middleware staticMiddleware = new StaticMiddleware(root, directoryController, directoryController);
        Controller application = staticMiddleware.use(router);
        connectionHandler = new ConnectionHandler(socket, logger, application, requestParser, responseSerializer);
    }

    @Test
    public void handlesValidRequestFromSocket() {
        String directoryString = "GET / HTTP/1.1";
        Request request = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/");
            put("version", "HTTP/1.1");
        }});
        Response directoryResponse = directoryController.createResponse(request);
        byte[] directoryBytes = responseSerializer.convertToBytes(directoryResponse);

        socket.setRequest(directoryString);
        connectionHandler.run();

        assertArrayEquals(directoryBytes, socket.getResponseReceived());
    }

    @Test
    public void handlesInvalidRequestFromSocket() {
        String notFoundString = "GET /snarf.jpg HTTP/1.1";
        Response response = new ResponseBuilder()
                                .addStatus(404)
                                .addHeader("Content-Type", "text/html")
                                .addBody(StatusCodes.retrieve(404))
                                .build();
        byte[] notFoundBytes = responseSerializer.convertToBytes(response);

        socket.setRequest(notFoundString);
        connectionHandler.run();

        assertArrayEquals(notFoundBytes, socket.getResponseReceived());
    }
}