package org.zaproxy.zap.extension.filetester.models;

import org.parosproxy.paros.Constant;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class Reporter {
    private static Reporter soleModel;
    protected static final String PREFIX = "filetester";
    private List<IDownloadedFile> uncompletedFiles = new LinkedList<>();

    public Reporter() {
        if (soleModel != null) {
            throw new RuntimeException(
                    "Use getInstance() method to get the single instance of this class.");
        }
    }

    public static Reporter getSingleton() {
        if (soleModel == null) {
            soleModel = new Reporter();
        }
        return soleModel;
    }


    public void addFile(IDownloadedFile file) {
        uncompletedFiles.add(file);
    }


    /**
     * Creates a text file from the report of the tests performed on the downloaded files.
     *
     * @param report List of latest file results
     * @throws IOException if cannot read the input stream.
     */
    private void createReport(List<IDownloadedFile> report) throws IOException {
        try (FileWriter fw = new FileWriter(Constant.messages.getString(PREFIX + ".report.name"), true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {
            for (IDownloadedFile r : report) {
                for (FileTestResult res : r.getTestResults()) {
                    String output = String.format(Constant.messages.getString(PREFIX + ".report.syntax"),
                            r.getName(), res.getName(), res.getResult(), res.getRemarks() != null ? res.getRemarks() : "");
                    pw.println(output);
                }
            }
        }
    }

    /**
     * Removes the files from list of uncompleted files if their testing is complete.
     *
     * @param files List of files with latest results.
     */
    private void updateFileLists(List<IDownloadedFile> files) {
        for (IDownloadedFile file : files) {
            if (file.isCompleted()) {
                uncompletedFiles.remove(file);
            }
        }
    }

    //      * Generates a report of files with the latest test results;
    public List<IDownloadedFile> generateReport() throws IOException {
        List<IDownloadedFile> completedFiles = new LinkedList<>();
        for (IDownloadedFile file : uncompletedFiles) {
            List<FileTestResult> testResults = file.getTestResults();
            if (!testResults.isEmpty()) {
                completedFiles.add(file);
            }
        }
        return completedFiles;
    }

    public void getReport() {
        try {
            List<IDownloadedFile> reportFiles = generateReport();
            createReport(reportFiles);
            updateFileLists(reportFiles);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
