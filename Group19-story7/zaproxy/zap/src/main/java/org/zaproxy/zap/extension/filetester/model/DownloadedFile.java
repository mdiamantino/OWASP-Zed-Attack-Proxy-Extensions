package org.zaproxy.zap.extension.filetester.model;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public abstract class DownloadedFile implements IDownloadedFile {

    private String name;
    private InputStream file;
    private List<FileTestResult> testResults;
    protected boolean isCompleted;

    public DownloadedFile(String name, InputStream file) {
        this.file = file;
        this.name = name;
        testResults = new LinkedList<>();
        isCompleted = false;
    }

    public String getName() {
        return name;
    }

    public List<FileTestResult> getTestResults() {
        return testResults;
    }

    public InputStream getFile() {
        return file;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
