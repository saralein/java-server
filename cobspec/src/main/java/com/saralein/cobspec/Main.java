package com.saralein.cobspec;

import com.saralein.cobspec.controller.*;
import com.saralein.cobspec.controller.form.*;
import com.saralein.cobspec.data.FormStore;
import com.saralein.cobspec.router.RoutesBuilder;
import com.saralein.server.HttpServer;
import com.saralein.server.router.Routes;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        String home = System.getProperty("user.dir");
        HttpServer.start(args, home, Main::setupRoutes);
    }

    private static Routes setupRoutes(Path root) {
        FileHelper fileHelper = new FileHelper(root);
        FormStore formStore = new FormStore();
        FormBody formBody = new FormBody();
        FormModification formModification = new FormModification();

        return new RoutesBuilder(new DirectoryController(fileHelper), new FileController(fileHelper), new NotFoundController())
                    .addRoute("/redirect", "GET", new RedirectController())
                    .addRoute("/form", "GET", new FormGetController(formStore, formBody))
                    .addRoute("/form", "POST", new FormPostController(formStore, formBody, formModification))
                    .addRoute("/form", "PUT", new FormPutController(formStore, formBody, formModification))
                    .addRoute("/form", "DELETE", new FormDeleteController(formStore))
                    .build();
    }
}