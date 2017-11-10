package com.saralein.server.response;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
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
        return root + "/" + name;
    }

    public String getMimeType(String file) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(file);
    }

    private List<File> listDirectoryFiles(File directory) {
        File[] fileArray = directory.listFiles(
                file -> !file.isHidden());

        return Arrays.asList(fileArray);
    }

    public List<String> getFilenames(File directory) {
        List<String> fileNames = new ArrayList<>();

        for (File file: listDirectoryFiles(directory)) {
            fileNames.add(file.getName());
        }

        Collections.sort(fileNames);

        return fileNames;
    }
}