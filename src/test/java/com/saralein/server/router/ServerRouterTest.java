package com.saralein.server.router;

import com.saralein.server.Controller.DirectoryController;
import com.saralein.server.Controller.FileController;
import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.Controller.NotFoundResponse;
import java.io.File;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ServerRouterTest {
    private Request notFoundRequest;
    private byte[] notFoundResponse;

    private Request directoryRequest;
    private byte[] directoryResponse;

    private Request fileRequest;
    private byte[] fileResponse;

    private ServerRouter router;

    @Before
    public void setUp() {
        String rootPath = System.getProperty("user.dir") + "/" + "public";
        File root = new File(rootPath);
        SysFileHelper fileHelper = new SysFileHelper(root);
        RequestParser requestParser = new RequestParser();

        String nullString = "GET /snarf.jpg HTTP/1.1";
        notFoundRequest = requestParser.parse(nullString);
        notFoundResponse = new NotFoundResponse().createResponse();

        String directoryString = "GET / HTTP/1.1";
        directoryRequest = requestParser.parse(directoryString);
        File directoryFile = new File("public");
        directoryResponse = new DirectoryController(directoryFile, fileHelper).createResponse();

        String fileString = "GET /cheetara.jpg HTTP/1.1";
        fileRequest = requestParser.parse(fileString);
        File file = new File("public/cheetara.jpg");
        fileResponse = new FileController(fileRequest, file, fileHelper).createResponse();

        Routes routes = new Routes();
        router = new ServerRouter(routes, fileHelper);
    }

    @Test
    public void returnsNullResponseForNonExistentResources() {
        assertArrayEquals(notFoundResponse, router.resolveRequest(notFoundRequest));
    }

    @Test
    public void returnsDirectoryResponseForDirectory() {
        assertArrayEquals(directoryResponse, router.resolveRequest(directoryRequest));
    }

    @Test
    public void returnsFileResponseForFile() {
        assertArrayEquals(fileResponse, router.resolveRequest(fileRequest));
    }
}