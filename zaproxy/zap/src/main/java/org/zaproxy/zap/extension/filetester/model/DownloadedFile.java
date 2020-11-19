package org.zaproxy.zap.extension.filetester.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class DownloadedFile implements IDownloadedFile {
    private String name;
    private File file;
    private List<FileTestResult> testResults;

    public DownloadedFile(File file) {
        this.file = file;
        this.name = file.getName();
        testResults = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<FileTestResult> getTestResults() {
        return testResults;
    }

    public File getFile() {
        return file;
    }
}
