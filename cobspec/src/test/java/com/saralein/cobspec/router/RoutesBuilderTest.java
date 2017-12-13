package com.saralein.cobspec.router;

import com.saralein.cobspec.controller.DirectoryController;
import com.saralein.cobspec.controller.FileController;
import com.saralein.cobspec.controller.ClientErrorController;
import com.saralein.cobspec.FileHelper;
import com.saralein.server.router.Routes;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RoutesBuilderTest {
    private RoutesBuilder routesBuilder;
    private ClientErrorController clientErrorController;

    @Before
    public void setUp() {
        Path root = Paths.get(System.getProperty("user.dir") + "/" + "public");
        FileHelper fileHelper = new FileHelper(root);
        DirectoryController directoryController = new DirectoryController(fileHelper);
        FileController fileController = new FileController(fileHelper);
        clientErrorController = new ClientErrorController(new HashMap<>());

        routesBuilder = new RoutesBuilder(directoryController, fileController, clientErrorController);
    }

    @Test
    public void setsUpASingleRoute() {
        Routes routes = routesBuilder
                        .addRoute("/notFound", "GET", clientErrorController)
                        .build();

        assertTrue(routes.matchesRouteAndMethod("/notFound", "GET"));
    }

    @Test
    public void setsUpSingleRouteWithTwoVerbs() {
        Routes routes = routesBuilder
                        .addRoute("/notFound", "GET", clientErrorController)
                        .addRoute("/notFound", "POST", clientErrorController)
                        .build();

        assertTrue(routes.matchesRouteAndMethod("/notFound", "GET"));
        assertTrue(routes.matchesRouteAndMethod("/notFound", "POST"));
    }
}