package com.saralein.server.response;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SysFileHelper implements FileHelper {
    String root;

    public SysFileHelper(String root) {
        this.root = root;
    }

    public String getRelativePath(String name) {
        return "/" + root + "/" + name;
    }

    private File getDirectoryContents() {
        return new File(root);
    }

    private List<File> listDirectoryFiles() {
        File[] fileArray = getDirectoryContents().listFiles(
                file -> !file.getName().equals(".DS_Store"));

        return Arrays.asList(fileArray);
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
