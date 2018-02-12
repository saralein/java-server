package com.saralein.server.filesystem;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class FilePath {
    private Path root;
    private String separator;
    private String empty;

    public FilePath(Path root) {
        this.root = root;
        this.separator = FileSystems.getDefault().getSeparator();
        this.empty = "";
    }

    public Path absolute(String name) {
        String file = removeLeadingSlash(name);
        return root.resolve(file);
    }

    public String relative(String name, Path resource) {
        String resourceString = resource.toString();
        return resourceString.replace(rootPathToString(), empty) + separator + name;
    }

    private String removeLeadingSlash(String name) {
        return name.substring(1);
    }

    private String rootPathToString() {
        return root.toString();
    }
}
