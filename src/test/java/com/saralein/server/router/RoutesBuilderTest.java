package com.saralein.server.router;

import com.saralein.server.Controller.DirectoryController;
import com.saralein.server.Controller.FileController;
import com.saralein.server.Controller.NotFoundController;
import com.saralein.server.response.FileHelper;
import com.saralein.server.response.SysFileHelper;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RoutesBuilderTest {
    private RoutesBuilder routesBuilder;

    @Before
    public void setUp() {
        Path root = Paths.get(System.getProperty("user.dir") + "/" + "public");
        FileHelper fileHelper = new SysFileHelper(root);
        DirectoryController directoryController = new DirectoryController(fileHelper);
        FileController fileController = new FileController(fileHelper);
        NotFoundController notFoundController = new NotFoundController();

        routesBuilder = new RoutesBuilder(directoryController, fileController, notFoundController);
    }

    @Test
    public void setsUpASingleRoute() {
        Routes routes = routesBuilder
                        .addRoute("/notFound", "GET", new NotFoundController())
                        .build();

        assertTrue(routes.isRoute("/notFound", "GET"));
    }

    @Test
    public void setsUpSingleRouteWithTwoVerbs() {
        Routes routes = routesBuilder
                        .addRoute("/notFound", "GET", new NotFoundController())
                        .addRoute("/notFound", "POST", new NotFoundController())
                        .build();

        assertTrue(routes.isRoute("/notFound", "GET"));
        assertTrue(routes.isRoute("/notFound", "POST"));
    }
}