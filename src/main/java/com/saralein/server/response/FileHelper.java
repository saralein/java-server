package com.saralein.server.response;

import java.util.List;

public interface FileHelper {
    String getRelativePath(String name);
    List<String> getFilenames();
}
