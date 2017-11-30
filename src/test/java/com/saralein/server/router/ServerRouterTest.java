package com.saralein.server.router;

import com.saralein.server.Controller.DirectoryController;
import com.saralein.server.Controller.FileController;
import com.saralein.server.Controller.NotFoundController;
import com.saralein.server.request.Request;
import com.saralein.server.request.RequestParser;
import com.saralein.server.response.Header;
import com.saralein.server.response.Response;
import com.saralein.server.response.SysFileHelper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ServerRouterTest {
    private Request notFoundRequest;
    private Request directoryRequest;
    private Request fileRequest;
    private ServerRouter router;

    @Before
    public void setUp() {
        String rootPath = System.getProperty("user.dir") + "/" + "public";
        Path root = Paths.get(rootPath);
        SysFileHelper fileHelper = new SysFileHelper(root);

        String nullString = "GET /snarf.jpg HTTP/1.1";
        RequestParser nullParser = new RequestParser();
        notFoundRequest = nullParser.parse(nullString);

        String directoryString = "GET / HTTP/1.1";
        RequestParser directoryParser = new RequestParser();
        directoryRequest = directoryParser.parse(directoryString);

        String fileString = "GET /cheetara.jpg HTTP/1.1";
        RequestParser fileParser = new RequestParser();
        fileRequest = fileParser.parse(fileString);

        DirectoryController directoryController = new DirectoryController(fileHelper);
        FileController fileController = new FileController(fileHelper);
        NotFoundController notFoundController = new NotFoundController();
        Routes routes = new RoutesBuilder(directoryController, fileController, notFoundController).build();

        router = new ServerRouter(routes, fileHelper);
    }

    private byte[] getFileBytes(String filePath) {
        byte[] fileByte = null;

        try {
            Path file = Paths.get(filePath);
            fileByte = Files.readAllBytes(file);
        } catch (IOException e) {
            fail("Failed to createContents file bytes in test.");
        }

        return fileByte;
    }

    @Test
    public void returnsNullResponseForNonExistentResources() {
        Response response = router.resolveRequest(notFoundRequest);
        Header header = response.getHeader();

        assertEquals("HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n", header.formatToString());
    }

    @Test
    public void returnsDirectoryResponseForDirectory() {
        String body = "<li><a href=/cake.pdf>cake.pdf</a></li>" +
                "<li><a href=/cheetara.jpg>cheetara.jpg</a></li>" +
                "<li><a href=/marshmallow.gif>marshmallow.gif</a></li>" +
                "<li><a href=/recipe.txt>recipe.txt</a></li>" +
                "<li><a href=/sloths>sloths</a></li>";

        byte[] bodyArray = body.getBytes();

        Response response = router.resolveRequest(directoryRequest);

        assertArrayEquals(bodyArray, response.getBody());
    }

    @Test
    public void returnsFileResponseForFile() {
        Response response = router.resolveRequest(fileRequest);

        assertArrayEquals(getFileBytes("public/cheetara.jpg"), response.getBody());
    }
}