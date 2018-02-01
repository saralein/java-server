package com.saralein.server.connection;

import com.saralein.server.controller.Controller;
import com.saralein.server.controller.ErrorController;
import com.saralein.server.mocks.MockController;
import com.saralein.server.mocks.MockLogger;
import com.saralein.server.mocks.MockSocket;
import com.saralein.server.parameters.ParameterDecoder;
import com.saralein.server.parameters.ParameterParser;
import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.router.Router;
import com.saralein.server.router.Routes;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertArrayEquals;

public class ConnectionHandlerTest {
    private MockSocket socket;
    private ConnectionHandler connectionHandler;
    private String directoryString;
    private String notFoundString;
    private byte[] directoryBytes;
    private byte[] notFoundBytes;

    @Before
    public void setUp() {
        String rootPath = System.getProperty("user.dir") + "/src/test/public";
        Path root = Paths.get(rootPath);
        MockLogger logger = new MockLogger();
        RequestParser requestParser = new RequestParser(new ParameterParser(), new ParameterDecoder());
        ResponseSerializer responseSerializer = new ResponseSerializer();

        socket = new MockSocket();

        directoryString = "GET / HTTP/1.1";
        Request directoryRequest = new Request.Builder()
                .method("GET")
                .uri("/")
                .build();
        Controller directoryController = new MockController(200, "Directory response");

        Response directoryResponse = directoryController.respond(directoryRequest);
        directoryBytes = responseSerializer.convertToBytes(directoryResponse);

        notFoundString = "GET /snarf.jpg HTTP/1.1";
        Request notFoundRequest = new Request.Builder()
                .method("GET")
                .uri("/")
                .build();
        ErrorController notFoundController = new ErrorController();
        Response notFoundResponse = notFoundController.respond(notFoundRequest);
        notFoundBytes = responseSerializer.convertToBytes(notFoundResponse);

        Controller fileController = new MockController(200, "File response");
        Routes routes = new Routes();

        Router router = new Router(directoryController, fileController, notFoundController, routes, root);
        connectionHandler = new ConnectionHandler(socket, logger, router, requestParser, responseSerializer);
    }

    @Test
    public void getsRequestFromSocketAndSendsResponse() {
        socket.setRequest(directoryString);
        connectionHandler.run();

        assertArrayEquals(directoryBytes, socket.getResponseReceived());

        socket.setRequest(notFoundString);
        connectionHandler.run();

        assertArrayEquals(notFoundBytes, socket.getResponseReceived());
    }
}