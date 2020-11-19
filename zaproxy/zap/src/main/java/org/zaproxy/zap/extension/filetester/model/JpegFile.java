package org.zaproxy.zap.extension.filetester.model;

import java.io.File;
import java.io.IOException;

public class JpegFile extends DownloadedFile {

    public JpegFile(File file) {
        super(file);
    }

    @Override
    public boolean isValid() throws IOException {
        return false;
    }

}
