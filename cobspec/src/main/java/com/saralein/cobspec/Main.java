package com.saralein.cobspec;

import com.saralein.cobspec.controller.*;
import com.saralein.cobspec.controller.form.*;
import com.saralein.cobspec.controller.OptionsController;
import com.saralein.cobspec.data.FormStore;
import com.saralein.server.logger.ConnectionLogger;
import com.saralein.server.logger.Logger;
import com.saralein.server.protocol.Methods;
import com.saralein.cobspec.router.RoutesBuilder;
import com.saralein.server.HttpServer;
import com.saralein.server.router.Routes;
import com.saralein.cobspec.validation.ArgsValidation;
import com.saralein.cobspec.validation.DirectoryValidator;
import com.saralein.cobspec.validation.PortValidator;
import com.saralein.cobspec.validation.Validator;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String home = System.getProperty("user.dir");
        Logger logger = new ConnectionLogger(System.out);
        List<String> validationErrors = runValidationAndReturnErrors(args, home);

        if (validationErrors.isEmpty()) {
            ArgsParser argsParser = new ArgsParser(args);
            Integer port = argsParser.parsePort();
            Path root = argsParser.parseRoot(home);
            Routes routes = setupRoutes(root);
            HttpServer.start(port, root, routes, logger);
        } else {
            logger.log(String.join("\n", validationErrors));
        }
    }

    private static List<String> runValidationAndReturnErrors(String[] args, String home) {
        List<Validator> validators = new ArrayList<Validator>(){{
            add(new PortValidator());
            add(new DirectoryValidator(home));
        }};

        return new ArgsValidation(validators).validate(args);
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