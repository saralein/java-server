package com.saralein.cobspec;

import com.saralein.cobspec.controller.*;
import com.saralein.cobspec.controller.form.*;
import com.saralein.cobspec.data.FormStore;
import com.saralein.cobspec.data.LogStore;
import com.saralein.cobspec.logger.ApplicationLogger;
import com.saralein.cobspec.validation.ArgsValidation;
import com.saralein.cobspec.validation.DirectoryValidator;
import com.saralein.cobspec.validation.PortValidator;
import com.saralein.cobspec.validation.Validator;
import com.saralein.server.Application;
import com.saralein.server.authorization.Authorizer;
import com.saralein.server.controller.UnauthorizedController;
import com.saralein.server.logger.Logger;
import com.saralein.server.protocol.Methods;
import com.saralein.server.router.Routes;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String home = System.getProperty("user.dir");
        LogStore logStore = new LogStore();
        Logger logger = new ApplicationLogger(System.out, logStore);
        List<String> validationErrors = runValidationAndReturnErrors(args, home);

        if (validationErrors.isEmpty()) {
            ArgsParser argsParser = new ArgsParser(args);
            Integer port = argsParser.parsePort();
            Path root = argsParser.parseRoot(home);

            FormStore formStore = new FormStore();
            FormBody formBody = new FormBody();
            FormModification formModification = new FormModification();

            Authorizer authorizer = new Authorizer("admin", "hunter2");
            UnauthorizedController unauthorizedController = new UnauthorizedController("ServerCity");

            new Application(logger)
                    .config(new Routes()
                                .get("/redirect", new RedirectController())
                                .get("/form", new FormGetController(formStore, formBody))
                                .post("/form", new FormPostController(formStore, formBody, formModification))
                                .put("/form", new FormPutController(formStore, formBody, formModification))
                                .delete("/form", new FormDeleteController(formStore))
                                .options("/method_options", new OptionsController(Methods.allowNonDestructiveMethods()))
                                .get("/method_options", new DefaultController())
                                .put("/method_options", new DefaultController())
                                .post("/method_options", new DefaultController())
                                .head("/method_options", new DefaultController())
                                .options("/method_options2", new OptionsController(Methods.allowGetAndOptions()))
                                .get("/method_options2", new DefaultController())
                                .get("/tea", new DefaultController())
                                .get("/coffee", new CoffeeController())
                            .get("/logs", new LogController(logStore, authorizer, unauthorizedController)))
                    .start(port, root);
        } else {
            logger.fatal(String.join("\n", validationErrors));
        }
    }

    private static List<String> runValidationAndReturnErrors(String[] args, String home) {
        List<Validator> validators = new ArrayList<Validator>(){{
            add(new PortValidator());
            add(new DirectoryValidator(home));
        }};

        return new ArgsValidation(validators).validate(args);
    }
}