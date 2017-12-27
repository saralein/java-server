package com.saralein.server;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileHelper {
    private Path root;
    private String separator = FileSystems.getDefault().getSeparator();

    public FileHelper(Path root) {
        this.root = root;
    }

    public String createAbsolutePath(String name) {
        return root.toString() + separator + name;
    }

    public String createRelativeFilePath(String name, Path resource) {
        return removeRootPortionOfPath(resource) + separator + name;
    }

    public String determineMimeType(String file) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(file);
    }

    public List<String> listFileNames(Path directory) throws IOException {
        return Files.list(directory)
                .filter(this::isNotHidden)
                .map(this::formatName)
                .sorted()
                .collect(Collectors.toList());
    }

    private String removeRootPortionOfPath(Path resource) {
        return resource.toString().replace(root.toString(), "");
    }

    private String formatName(Path path) {
        String nameEnding = Files.isDirectory(path) ? "/" : "";
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