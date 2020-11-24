package org.zaproxy.zap.extension.filetester.model;

import java.util.LinkedList;
import java.util.List;

public class Report {
    public List<IDownloadedFile> generateReport(List<IDownloadedFile> files) {
        System.out.println(files.size());
        List<IDownloadedFile> completedFiles = new LinkedList<>();
        for (IDownloadedFile file: files) {
            List<FileTestResult> testResults =  file.getTestResults();
            if (! testResults.isEmpty()) {
                completedFiles.add(file);
            }
        }
        return completedFiles;
    }
}
