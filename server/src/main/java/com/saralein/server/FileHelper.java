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
    private static String separator = FileSystems.getDefault().getSeparator();
    private static String emptyString = "";
    private Path root;

    public FileHelper(Path root) {
        this.root = root;
    }

    public Path createAbsolutePath(String name) {
        return root.resolve(name.substring(1));
    }

    public String createRelativeFilePath(String name, Path resource) {
        String resourceString = resource.toString();
        return resourceString.replace(rootPathToString(), emptyString) + separator + name;
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

    public int getFileLength(Path file) throws IOException {
        return (int) Files.size(file);
    }

    private String rootPathToString() {
        return root.toString();
    }

    private String formatName(Path path) {
        String nameEnding = Files.isDirectory(path) ? separator : emptyString;
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