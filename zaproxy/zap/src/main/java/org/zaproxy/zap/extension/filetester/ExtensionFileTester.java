/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2020 The ZAP Development Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zaproxy.zap.extension.filetester;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;
import org.parosproxy.paros.network.HttpResponseHeader;
import org.parosproxy.paros.network.HttpSender;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.filetester.factory.FileFactory;
import org.zaproxy.zap.extension.filetester.model.FileTestResult;
import org.zaproxy.zap.extension.filetester.model.IDownloadedFile;
import org.zaproxy.zap.extension.filetester.model.Report;
import org.zaproxy.zap.network.HttpResponseBody;
import org.zaproxy.zap.network.HttpSenderListener;
import org.zaproxy.zap.view.ZapMenuItem;

import javax.swing.*;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * ExtensionFileTester is the entry point for the File Tester Extension. It operates on JPEG, PNG, ZIP, and EXE file types for different tests.
 */
public class ExtensionFileTester extends ExtensionAdaptor implements HttpSenderListener {
    public static final String NAME = "ExtensionFileTester";
    protected static final String PREFIX = "filetester";
    private static final Logger logger = Logger.getLogger(ExtensionFileTester.class);

    private final FileFactory factory;
    private final Collection<String> allowedContentTypes = Arrays.asList("image/jpeg", "image/png", "application/zip", "application/octet-stream", "application/x-msdownload");
    private final Collection<String> allowedExtensions = Arrays.asList("jpeg", "jpg", "png", "zip", "exe");

    private javax.swing.JMenu menuFileTester = null;
    private int enable_step = 0;
    private List<IDownloadedFile> uncompletedFiles;

    public ExtensionFileTester() {
        super(NAME);
        setI18nPrefix(PREFIX);
        factory = new FileFactory();
        uncompletedFiles = new LinkedList<>();
    }

    @Override
    public void hook(ExtensionHook extensionHook) {
        super.hook(extensionHook);
        extensionHook.addHttpSenderListener(this);
        if (getView() != null) {
            extensionHook.getHookMenu().addNewMenu(getFileTesterMenu());
        }
    }

    @Override
    public boolean canUnload() {
        return true;
    }

    @Override
    public void unload() {
        super.unload();
    }

    @Override
    public String getDescription() {
        return Constant.messages.getString(PREFIX + ".desc");
    }

    private ZapMenuItem getMenuOptionFileTesterToggle() {
        ZapMenuItem menuFileTester = new ZapMenuItem(PREFIX + ".menu.toggle");
        menuFileTester.addActionListener(e -> {
            String message = (enable_step % 2 == 0 ? Constant.messages.getString(PREFIX + ".enable") : Constant.messages.getString(PREFIX + ".disable"));
            String title = "Alert!";
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
            this.enable_step += 1;
            logger.info(message);
        });
        return menuFileTester;
    }

    private ZapMenuItem getMenuOptionHelp() {
        ZapMenuItem menuHelp = new ZapMenuItem(PREFIX + ".menu.help");
        menuHelp.addActionListener(
                e -> {
                    DocDialog dialog =
                            new DocDialog(
                                    Objects.requireNonNull(View.getSingleton()).getMainFrame(),
                                    true);
                    dialog.setVisible(true);
                });
        return menuHelp;
    }

    private ZapMenuItem getReportOption() {
        ZapMenuItem menuReport = new ZapMenuItem(PREFIX + ".menu.report");
        menuReport.addActionListener(
                e -> {
                    try {
                        List<IDownloadedFile> reportFiles = generateReport();
                        createReport(reportFiles);
                        updateFileLists(reportFiles);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
        return menuReport;
    }

    private void setUpFileTesterMenu() {
        if (menuFileTester != null) {
            menuFileTester.add(getMenuOptionFileTesterToggle()); // Adding enable disable button
            menuFileTester.add(getMenuOptionHelp()); // Adding Help button
            menuFileTester.add(getReportOption()); //Adding the report button
        }
    }

    private javax.swing.JMenu getFileTesterMenu() {
        if (menuFileTester == null) {
            menuFileTester = new javax.swing.JMenu();
            menuFileTester.setText(Constant.messages.getString(PREFIX + ".menu.title"));
            setUpFileTesterMenu();
        }
        return menuFileTester;
    }

    @Override
    public int getListenerOrder() {
        return 1;
    }

    @Override
    public void onHttpRequestSend(HttpMessage msg, int initiator, HttpSender sender) {
        scan(msg);
    }

    @Override
    public void onHttpResponseReceive(HttpMessage msg, int initiator, HttpSender sender) {
        scan(msg);
    }

    private void scan(HttpMessage msg) {
        if (this.enable_step == 2) {
            return;
        }
        try {
            HttpRequestHeader requestHeader = msg.getRequestHeader();
            HttpResponseHeader responseHeader = msg.getResponseHeader();
            String fileName = URLDecoder.decode(requestHeader.getURI().getName(), StandardCharsets.UTF_8.toString());
            if (responseHeader.hasContentType(allowedContentTypes.toArray(new String[0])) && FilenameUtils.isExtension(fileName, allowedExtensions)) {
                HttpResponseBody responseBody = msg.getResponseBody();
                InputStream fileStream = new ByteArrayInputStream(responseBody.getBytes());
                IDownloadedFile file = factory.createdDownloadedFile(fileName, fileStream);
                if (!file.isValid()) {
                    String title = Constant.messages.getString(PREFIX + ".menu.alert.title");
                    String description = String.format(Constant.messages.getString(PREFIX + ".menu.alert.desc"), fileName);
                    JOptionPane.showMessageDialog(null, description, title, JOptionPane.INFORMATION_MESSAGE);
                }
                uncompletedFiles.add(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a report of files with the latest test results;
     *
     * @return List of files with latest test results.
     * @throws IOException if cannot read the input stream.
     */
    private List<IDownloadedFile> generateReport() throws IOException {
        Report report = new Report();
        return report.generateReport(uncompletedFiles);
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
}
