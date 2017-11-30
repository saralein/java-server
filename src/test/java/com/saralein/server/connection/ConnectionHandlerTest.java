package com.saralein.server.connection;

import com.saralein.server.Controller.FileController;
import com.saralein.server.mocks.MockLogger;
import com.saralein.server.mocks.MockSocket;
import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import com.saralein.server.Controller.DirectoryController;
import com.saralein.server.response.Response;
import com.saralein.server.response.ResponseSerializer;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.Controller.NotFoundController;
import com.saralein.server.router.Routes;
import com.saralein.server.router.RoutesBuilder;
import com.saralein.server.router.ServerRouter;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class ConnectionHandlerTest {
    private MockSocket socket;
    private ConnectionHandler connectionHandler;
    private String directoryString;
    private String notFoundString;
    private byte[] directoryBytes;
    private byte[] notFoundBytes;

    @Before
    public void setUp() {
        String rootPath = System.getProperty("user.dir") + "/" + "public";
        Path root = Paths.get(rootPath);
        MockLogger logger = new MockLogger();
        SysFileHelper fileHelper = new SysFileHelper(root);
        RequestParser requestParser = new RequestParser();
        ResponseSerializer responseSerializer = new ResponseSerializer();

        socket = new MockSocket();

        directoryString = "GET / HTTP/1.1";
        Request directoryRequest = new Request(new HashMap<String, String>(){{
            put("method", "GET");
            put("uri", "/");
            put("version", "HTTP/1.1");
        }});
        DirectoryController directoryController = new DirectoryController(fileHelper);
        Response directoryResponse = directoryController.createResponse(directoryRequest);
        directoryBytes = responseSerializer.convertToBytes(directoryResponse);

        notFoundString = "GET /snarf.jpg HTTP/1.1";
        Request notFoundRequest = new Request(new HashMap<String, String>() {{
            put("method", "GET");
            put("uri", "/snarf.jpg");
            put("version", "HTTP/1.1");
        }});
        NotFoundController notFoundController = new NotFoundController();
        Response notFoundResponse = notFoundController.createResponse(notFoundRequest);
        notFoundBytes = responseSerializer.convertToBytes(notFoundResponse);

        FileController fileController = new FileController(fileHelper);
        Routes routes = new RoutesBuilder(directoryController, fileController, notFoundController).build();

        ServerRouter router = new ServerRouter(routes, fileHelper);
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