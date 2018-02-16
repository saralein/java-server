package com.saralein.server.filesystem;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Directory implements Resource {
    private String separator;
    private String empty;

    public Directory() {
        this.separator = FileSystems.getDefault().getSeparator();
        this.empty = "";
    }

    @Override
    public boolean exists(Path directory) {
        return Files.exists(directory) && Files.isDirectory(directory);
    }

    public List<String> listFileNames(Path directory) throws IOException {
        try (Stream<Path> files = Files.list(directory)) {
            return files.filter(this::isNotHidden)
                    .map(this::formatName)
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    private String formatName(Path path) {
        String nameEnding = Files.isDirectory(path) ? separator : empty;
        return path.getFileName().toString() + nameEnding;
    }

    private boolean isNotHidden(Path path) {
        try {
            return !Files.isHidden(path);
        } catch (IOException e) {
            return false;
        }
    }
}
