package com.saralein.server.filesystem;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class ServerPaths {
    private Path root;
    private String separator;
    private String emptyString;

    public ServerPaths(Path root) {
        this.root = root;
        this.separator = FileSystems.getDefault().getSeparator();
        this.emptyString = "";
    }

    public Path createAbsolutePath(String name) {
        return root.resolve(name.substring(1));
    }

    public String createRelativePathString(String name, Path resource) {
        String target = resource.toString();
        return target.replace(rootPathToString(), emptyString) + separator + name;
    }

    private String rootPathToString() {
        return root.toString();
    }
}
