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

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.network.*;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.filetester.factory.FileFactory;
import org.zaproxy.zap.extension.filetester.model.FileTestResult;
import org.zaproxy.zap.extension.filetester.model.IDownloadedFile;
import org.zaproxy.zap.extension.filetester.model.Report;
import org.zaproxy.zap.network.HttpResponseBody;
import org.zaproxy.zap.network.HttpSenderListener;
import org.zaproxy.zap.view.ZapMenuItem;
import org.apache.log4j.Logger;

public class ExtensionFileTester extends ExtensionAdaptor implements HttpSenderListener {
    public static final String NAME = "ExtensionFileTester";
    protected static final String PREFIX = "filetester";
    private javax.swing.JMenu menuPolicyPlugin = null;
    private int enable_step = 0;
    private static Logger log = Logger.getLogger(ExtensionFileTester.class);

    private final FileFactory factory;
    private List<IDownloadedFile> uncompletedFiles;
    private List<IDownloadedFile> completedFiles;

    public ExtensionFileTester() {
        super(NAME);
        setI18nPrefix(PREFIX);
        factory = new FileFactory();
        uncompletedFiles = new LinkedList<>();
        completedFiles = new LinkedList<>();
    }

    @Override
    public void hook(ExtensionHook extensionHook) {
        super.hook(extensionHook);

        extensionHook.addHttpSenderListener(this);
        if (getView() != null) {
            extensionHook.getHookMenu().addNewMenu(getMenuPolicyPlugin());
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

    /**
     * View method used to retrieve the loaded file from the view through a graphical file chooser.
     * Only JAR files are accepted. When a valid file is picked, it is passed to the controller
     * (PolicyLoaderController).
     */


    private ZapMenuItem getMenuOptionLoadPolicy() {
        ZapMenuItem menuLoadPolicy = new ZapMenuItem("Activate/Deactivate Extension");
        menuLoadPolicy.addActionListener(e -> {
            this.enable_step += 1;
            if(this.enable_step == 1) {
                log.info("FileTester is now enabled");
                log.info("Value is: " + this.enable_step);
            }
            else if(this.enable_step == 2){
                log.info("FileTester is now disabled.");
                log.info("Value is: " + this.enable_step);
                enable_step =0;
            }
        });
        return menuLoadPolicy;
    }

    private ZapMenuItem getMenuOptionHelp() {
        ZapMenuItem menuHelp = new ZapMenuItem("Help (Documentation)");
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
        ZapMenuItem menuReport = new ZapMenuItem("Get Report of Scans");
        menuReport.addActionListener(
                e -> {
                    List<IDownloadedFile> reportFiles = generateReport();
                    createReport(reportFiles);
                    updateFileLists(reportFiles);
                });
        return menuReport;
    }

    private void setUpPluginMenu() {
        if (menuPolicyPlugin != null) {
            menuPolicyPlugin.add(getMenuOptionLoadPolicy()); // Adding loading button
            menuPolicyPlugin.add(getMenuOptionHelp()); // Adding Help button
            menuPolicyPlugin.add(getReportOption()); //Adding the report button
        }
    }

    private javax.swing.JMenu getMenuPolicyPlugin() {
        if (menuPolicyPlugin == null) {
            menuPolicyPlugin = new javax.swing.JMenu();
            menuPolicyPlugin.setText("Extension FileTester");
            setUpPluginMenu();
        }
        return menuPolicyPlugin;
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

    private boolean scan(HttpMessage msg) {
        if (this.enable_step  == 2) { // The extension is disabled so we do not react
            return true;
        }
        String site = msg.getRequestHeader().getHostName() + ":" + msg.getRequestHeader().getHostPort();
//        log.info(site);

        try {
            HttpRequestHeader requestHeader = msg.getRequestHeader();
            HttpResponseHeader responseHeader = msg.getResponseHeader();
            String fileName = URLDecoder.decode(requestHeader.getURI().getName(), StandardCharsets.UTF_8.toString());
            if (responseHeader.hasContentType("image/jpeg", "image/png", "application/zip", "application/octet-stream", "application/x-msdownload")
                && FilenameUtils.isExtension(fileName, "jpeg", "jpg", "png", "zip", "exe")) {
                HttpResponseBody responseBody = msg.getResponseBody();
                log.info(requestHeader.getURI() + "\t" + fileName + "\t" + FilenameUtils.getExtension(fileName));
                InputStream fileStream = new ByteArrayInputStream(responseBody.getBytes());
                IDownloadedFile file = factory.createdDownloadedFile(fileName, fileStream);
                if (file.isValid()) {
                    System.out.println("valid");
                } else {
                    // popUp();
                    System.out.println("invalid");
                }
                uncompletedFiles.add(file);
            }
        } catch (Exception e) {
//                e.printStackTrace();
        }
        return true;
    }

    private List<IDownloadedFile> generateReport() {
        Report report = new Report();
        return report.generateReport(uncompletedFiles);
    }

    private void updateFileLists(List<IDownloadedFile> files) {
        for(IDownloadedFile file: files) {
            if (file.isCompleted()) {
                uncompletedFiles.remove(file);
                completedFiles.add(file);
            }
        }
    }

    private void createReport(List<IDownloadedFile> report) {
        try(FileWriter fw = new FileWriter("file_tester_report.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
            {
                for (IDownloadedFile r: report) {
                    for (FileTestResult res: r.getTestResults()) {
                        String output = String.format("File Name: %s\tTest Name: %s\tTest Result: %b\tTest Remarks: %s",
                                r.getName(),res.getName(),res.getResult(),res.getRemarks()!=null?res.getRemarks():"");
                        out.println(output);
                    }
                }
            } catch (IOException e) {
        }
    }
}
