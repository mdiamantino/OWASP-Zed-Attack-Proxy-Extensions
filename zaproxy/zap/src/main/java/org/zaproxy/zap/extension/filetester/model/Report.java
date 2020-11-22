package org.zaproxy.zap.extension.filetester.model;

import java.util.LinkedList;
import java.util.List;

public class Report {
    public List<DownloadedFile> generateReport(List<DownloadedFile> files) {
        List<DownloadedFile> completedFiles = new LinkedList<>();
        for (DownloadedFile file: files) {
            List<FileTestResult> testResults =  file.getTestResults();
            if (! testResults.isEmpty()) {
                completedFiles.add(file);
            }
        }
        return completedFiles;
    }
}
