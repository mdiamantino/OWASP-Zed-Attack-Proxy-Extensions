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
package org.zaproxy.zap.extension.filetester.controllers;

import org.apache.commons.io.FilenameUtils;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;
import org.parosproxy.paros.network.HttpResponseHeader;
import org.parosproxy.paros.network.HttpSender;
import org.zaproxy.zap.extension.filetester.models.FileFactory;
import org.zaproxy.zap.extension.filetester.models.IDownloadedFile;
import org.zaproxy.zap.extension.filetester.models.Reporter;
import org.zaproxy.zap.network.HttpResponseBody;
import org.zaproxy.zap.network.HttpSenderListener;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;

/**
 * FileTesterScanner is the entry point for the File Tester Extension. It operates on JPEG, PNG, ZIP, and EXE file types for different tests.
 */
public class FileTesterScanner implements HttpSenderListener {
    protected static final String PREFIX = "filetester";
    private static FileTesterScanner soleController;
    private final FileFactory factory;
    private final Collection<String> allowedContentTypes = Arrays.asList("image/jpeg", "image/png", "application/zip", "application/octet-stream", "application/x-msdownload");
    private final Collection<String> allowedExtensions = Arrays.asList("jpeg", "jpg", "png", "zip", "exe");
    private boolean isEnabled = false;

    public FileTesterScanner() {
        if (soleController != null) {
            throw new RuntimeException(
                    "Use getInstance() method to get the single instance of this class.");
        }
        factory = new FileFactory();
    }

    public static FileTesterScanner getSingleton() {
        if (soleController == null) {
            soleController = new FileTesterScanner();
        }
        return soleController;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Toggle the behaviour of the extension.
     */
    public void enableOrDisableListener() {
        isEnabled = !(isEnabled);
        String message = (isEnabled ? Constant.messages.getString(PREFIX + ".enable") : Constant.messages.getString(PREFIX + ".disable"));
        String title = "Alert!";
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public int getListenerOrder() {
        return 1;
    }

    @Override
    public void onHttpRequestSend(HttpMessage msg, int initiator, HttpSender sender) {
    }

    @Override
    public void onHttpResponseReceive(HttpMessage msg, int initiator, HttpSender sender) {
        if (!isEnabled) {
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
                Reporter.getSingleton().addFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
