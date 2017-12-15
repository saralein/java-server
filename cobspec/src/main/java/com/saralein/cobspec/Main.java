package com.saralein.cobspec;

import com.saralein.cobspec.controller.*;
import com.saralein.cobspec.controller.form.*;
import com.saralein.cobspec.controller.OptionsController;
import com.saralein.cobspec.data.FormStore;
import com.saralein.server.protocol.Methods;
import com.saralein.cobspec.router.RoutesBuilder;
import com.saralein.server.HttpServer;
import com.saralein.server.router.Routes;
import java.nio.file.Path;
import java.util.HashMap;

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

        HashMap<Integer, String> errorMessages = new HashMap<Integer, String>(){{
            put(404, "<center><h1>404</h1>Page not found.</center>");
            put(405, "");
        }};

        return new RoutesBuilder(new DirectoryController(fileHelper),
                                 new FileController(fileHelper),
                                 new ClientErrorController(errorMessages))
                    .addRoute("/redirect", Methods.GET.name(), new RedirectController())
                    .addRoute("/form", Methods.GET.name(), new FormGetController(formStore, formBody))
                    .addRoute("/form", Methods.POST.name(), new FormPostController(formStore, formBody, formModification))
                    .addRoute("/form", Methods.PUT.name(), new FormPutController(formStore, formBody, formModification))
                    .addRoute("/form", Methods.DELETE.name(), new FormDeleteController(formStore))
                    .addRoute("/method_options", Methods.OPTIONS.name(), new OptionsController(Methods.allowNonDestructiveMethods()))
                    .addRoute("/method_options", Methods.GET.name(), new DefaultController())
                    .addRoute("/method_options", Methods.PUT.name(), new DefaultController())
                    .addRoute("/method_options", Methods.POST.name(), new DefaultController())
                    .addRoute("/method_options", Methods.HEAD.name(), new DefaultController())
                    .addRoute("/method_options2", Methods.OPTIONS.name(), new OptionsController(Methods.allowGetAndOptions()))
                    .addRoute("/method_options2", Methods.GET.name(), new DefaultController())
                    .addRoute("/tea", Methods.GET.name(), new DefaultController())
                    .addRoute("/coffee", Methods.GET.name(), new CoffeeController())
                    .build();
    }
}