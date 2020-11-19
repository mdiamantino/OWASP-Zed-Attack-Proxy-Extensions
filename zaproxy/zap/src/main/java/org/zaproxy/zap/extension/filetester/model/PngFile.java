package org.zaproxy.zap.extension.filetester.model;


import java.io.File;
import java.io.IOException;

public class PngFile extends DownloadedFile {
    public PngFile(File file) {
        super(file);
    }

    @Override
    public boolean isValid() throws IOException {
        return false;
    }
}
