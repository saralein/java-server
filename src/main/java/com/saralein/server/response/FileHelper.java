package com.saralein.server.response;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileHelper {
    String root;

    public FileHelper(String root) {
        this.root = root;
    }

    public String getRelativePath(String name) {
        return "/" + root + "/" + name;
    }

    private File getDirectoryContents() {
        return new File(root);
    }

    private File[] listDirectoryFiles() {
        return getDirectoryContents().listFiles(
            file -> !file.getName().equals(".DS_Store"));
    }

    public List<String> getFilenames() {
        List<String> fileNames = new ArrayList<>();

        for (File file: listDirectoryFiles()) {
            fileNames.add(file.getName());
        }

        Collections.sort(fileNames);

        return fileNames;
    }
}
