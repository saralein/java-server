package com.saralein.server.handler;

import com.saralein.server.filesystem.Directory;
import com.saralein.server.filesystem.FilePath;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryHandlerTest {
    private byte[] body;
    private DirectoryHandler directoryHandler;
    private Request request;

    @Before
    public void setUp() {
        body = ("<li><a href=/cake.pdf>cake.pdf</a></li>" +
                "<li><a href=/cheetara.jpg>cheetara.jpg</a></li>" +
                "<li><a href=/marshmallow.gif>marshmallow.gif</a></li>" +
                "<li><a href=/recipe.txt>recipe.txt</a></li>" +
                "<li><a href=/sloths/>sloths/</a></li>")
                .getBytes();

        String rootPath = System.getProperty("user.dir") + "/src/test/public";
        Path root = Paths.get(rootPath);
        request = new Request.Builder()
                .method("GET")
                .uri("/")
                .build();

        directoryHandler = new DirectoryHandler(new Directory(), new FilePath(root));
    }

    @Test
    public void returnsDirectoryResponse() throws IOException {
        Response expected = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/html")
                .body(body)
                .build();
        Response response = directoryHandler.handle(request);

        assert (response.equals(expected));
    }
}
