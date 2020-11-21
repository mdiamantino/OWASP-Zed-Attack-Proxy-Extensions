package org.zaproxy.zap.extension.filetester.model;


import java.io.File;
import java.io.IOException;

public class ZipFile extends DownloadedFile {
    public ZipFile(File file) {
        super(file);
    }

    @Override
    public boolean isValid() throws IOException {
        return false;
    }
}
