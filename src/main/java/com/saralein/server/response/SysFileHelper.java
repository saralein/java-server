package com.saralein.server.response;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SysFileHelper implements FileHelper {
    private Path root;
    private String separator = FileSystems.getDefault().getSeparator();

    public SysFileHelper(Path root) {
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
        List<String> fileNames = new ArrayList<>();
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory);

        for (Path path : directoryStream) {
            listFileIfNotHidden(path, fileNames);
        }

        Collections.sort(fileNames);

        return fileNames;
    }

    private String removeRootPortionOfPath(Path resource) {
        return resource.toString().replace(root.toString(), "");
    }

    private void listFileIfNotHidden(Path path, List<String> fileNames) throws IOException {
        if (!Files.isHidden(path)) {
            fileNames.add(path.getFileName().toString());
        }
    }
}