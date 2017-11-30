package com.saralein.server.mocks;

import com.saralein.server.response.SysFileHelper;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

class MockFileHelper extends SysFileHelper {
    MockFileHelper() {
        super(Paths.get(System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + "public"));
    }
}
