package com.saralein.server.mocks;

import com.saralein.server.response.SysFileHelper;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

public class MockFileHelper extends SysFileHelper {
    public MockFileHelper() {
        super(Paths.get(System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + "public"));
    }
}
