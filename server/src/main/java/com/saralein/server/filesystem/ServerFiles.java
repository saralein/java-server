package com.saralein.server.filesystem;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ServerFiles implements FilesInfo {
    private String separator;
    private String emptyString;

    public ServerFiles() {
        this.separator = FileSystems.getDefault().getSeparator();
        this.emptyString = "";
    }

    @Override
    public String determineMimeType(String file) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(file);
    }

    @Override
    public int getFileLength(Path file) throws IOException {
        return (int) Files.size(file);
    }

    @Override
    public List<String> listFileNames(Path directory) throws IOException {
        return Files.list(directory)
                .filter(this::isNotHidden)
                .map(this::formatName)
                .sorted()
                .collect(Collectors.toList());
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