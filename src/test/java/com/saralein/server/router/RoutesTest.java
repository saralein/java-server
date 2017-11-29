package com.saralein.server.router;

import com.saralein.server.Controller.DirectoryController;
import com.saralein.server.Controller.FileController;
import com.saralein.server.Controller.NotFoundController;
import com.saralein.server.Controller.RedirectController;
import com.saralein.server.response.FileHelper;
import com.saralein.server.response.SysFileHelper;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class RoutesTest {
    private Path root = Paths.get(System.getProperty("user.dir") + "/" + "public");
    private FileHelper fileHelper = new SysFileHelper(root);
    DirectoryController directoryController = new DirectoryController(fileHelper);
    FileController fileController = new FileController(fileHelper);
    NotFoundController notFoundController = new NotFoundController();

    private Routes routes = new RoutesBuilder(directoryController, fileController, notFoundController)
                                .addRoute("/redirect", "GET", new RedirectController())
                                .build();

    @Test
    public void checksIfRouteExists() {
        assertFalse(routes.isRoute("/doggos", "GET"));
        assertTrue(routes.isRoute("/redirect", "GET"));
    }

    @Test
    public void returnsControllerForRoute() {
        assertEquals(RedirectController.class, routes.retrieveController("/redirect", "GET").getClass());
    }

    @Test
    public void returnsDirectoryController() {
        assertEquals(DirectoryController.class, routes.retrieveDirectoryController().getClass());
    }

    @Test
    public void returnsFileController() {
        assertEquals(FileController.class, routes.retrieveFileController().getClass());
    }

    @Test
    public void returns404Controller() {
        assertEquals(NotFoundController.class, routes.retrieve404Controller().getClass());
    }
}