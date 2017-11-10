package com.saralein.server.connection;

import com.saralein.server.mocks.MockLogger;
import com.saralein.server.mocks.MockSocket;

import com.saralein.server.request.RequestParser;
import com.saralein.server.response.DirectoryResponse;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.response.NotFoundResponse;
import com.saralein.server.router.ServerRouter;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ConnectionHandlerTest {
    private MockSocket socket;
    private ConnectionHandler connectionHandler;
    private String directoryString;
    private String notFoundString;
    private byte[] directoryResponse;
    private byte[] notFoundResponse;

    @Before
    public void setUp() {
        SysFileHelper fileHelper = new SysFileHelper("public");
        MockLogger logger = new MockLogger();
        RequestParser requestParser = new RequestParser();

        socket = new MockSocket();

        directoryString = "GET / HTTP/1.1";
        File directoryFile = new File("public");
        directoryResponse = new DirectoryResponse(directoryFile, fileHelper).createResponse();

        notFoundString = "GET /snarf.jpg HTTP/1.1";
        notFoundResponse = new NotFoundResponse().createResponse();

        ServerRouter router = new ServerRouter(fileHelper);
        connectionHandler = new ConnectionHandler(socket, logger, router, requestParser);
    }

    @Test
    public void getsRequestFromSocketAndSendsResponse() {
        socket.setRequest(directoryString);
        connectionHandler.run();

        assertArrayEquals(directoryResponse, socket.getResponseReceived());

        socket.setRequest(notFoundString);
        connectionHandler.run();

        assertArrayEquals(notFoundResponse, socket.getResponseReceived());
    }
}