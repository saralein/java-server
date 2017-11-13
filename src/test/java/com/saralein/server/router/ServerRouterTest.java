package com.saralein.server.router;

import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.DirectoryResponse;
import com.saralein.server.response.SysFileHelper;
import com.saralein.server.response.FileResponse;
import com.saralein.server.response.NotFoundResponse;
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
        SysFileHelper fileHelper = new SysFileHelper("public");

        String nullString = "GET /snarf.jpg HTTP/1.1";
        notFoundRequest = new Request(new RequestParser().parse(nullString));
        notFoundResponse = new NotFoundResponse().createResponse();

        String directoryString = "GET / HTTP/1.1";
        directoryRequest = new Request(new RequestParser().parse(directoryString));
        File directoryFile = new File("public");
        directoryResponse = new DirectoryResponse(directoryFile, fileHelper).createResponse();

        String fileString = "GET /cheetara.jpg HTTP/1.1";
        fileRequest = new Request(new RequestParser().parse(fileString));
        File file = new File("public/cheetara.jpg");
        fileResponse = new FileResponse(fileRequest, file, fileHelper).createResponse();

        router = new ServerRouter(fileHelper);
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