package org.zaproxy.zap.extension.filetester.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExeFile extends DownloadedFile {
    public ExeFile(File file) {
        super(file);
    }

    @Override
    public boolean isValid() throws IOException {
        return false;
    }

    @Override
    public List<FileTestResult> getTestResults() {
        return null;
    }
}
