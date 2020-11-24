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
package org.zaproxy.zap.extension.filetester.models.fileTypes;

import net.sf.json.JSONObject;
import org.parosproxy.paros.Constant;
import org.zaproxy.zap.extension.filetester.models.DownloadedFile;
import org.zaproxy.zap.extension.filetester.models.FileTestResult;
import org.zaproxy.zap.extension.filetester.models.httpUtils.HttpUtility;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ExeFile manages the validation of EXE file types by performing a virus scan through an external API.
 */
public class ExeFile extends DownloadedFile {

    private static final String API_KEY = Constant.messages.getString("filetester.exe.scan.key");
    private static final String GET_URL = Constant.messages.getString("filetester.exe.scan.get");
    private static final String POST_URL = Constant.messages.getString("filetester.exe.scan.post");
    private static final String TEST_VIRUS_DETECTION = Constant.messages.getString("filetester.exe.scan");

    private Map<String, String> params;
    private Map<String, InputStream> fileParams;
    private String scanId;
    private boolean virusScanCompleted;
    private List<FileTestResult> testResults;
    private FileTestResult virusDetection;

    public ExeFile(String name, InputStream file) {
        super(name, file);
        params = new HashMap<>();
        params.put("apikey", API_KEY);
        fileParams = new HashMap<>();
        fileParams.put("file", file);
        testResults = new LinkedList<>();
        virusDetection = new FileTestResult(TEST_VIRUS_DETECTION);
    }

    @Override
    public boolean isValid() throws IOException {
        HttpUtility httpUtility = new HttpUtility();
        JSONObject postRequest = httpUtility.postRequest(POST_URL, params, fileParams, this.getName());
        scanId = (String) postRequest.get("scan_id");
        return true;
    }

    @Override
    public List<FileTestResult> getTestResults() throws IOException {
        if (isCompleted) {
            return testResults;
        }
        if (!virusScanCompleted) {
            JSONObject results = getScanResults();
            if (!results.isEmpty()) {
                int responseCode = (int) results.get("response_code");
                testResults.remove(virusDetection);
                if (responseCode == 1) {
                    String scanResultMessage = (String) results.get("verbose_msg");
                    int positives = (int) results.get("positives");
                    if (positives > 0) {
                        int total = (int) results.get("total");
                        virusDetection.setResult(true);
                        virusDetection.setRemarks(String.format("%s: %d out of %d scans flagged the file as a virus.",
                                scanResultMessage, positives, total));
                    } else {
                        virusDetection.setResult(false);
                        virusDetection.setRemarks("");
                    }
                    virusScanCompleted = true;
                    isCompleted = true;
                } else {
                    // the scan is still queued
                    virusDetection.setRemarks("Virus scan in progress.");
                }
                testResults.add(virusDetection);
            }
        }
        return testResults;
    }

    /**
     * Performs a HTTP GET request to get the result for the virus scan.
     *
     * @return The response from the API in a JSONObject format.
     * @throws IOException if cannot read the input stream.
     */
    private JSONObject getScanResults() throws IOException {
        String getUrl = String.format(GET_URL, API_KEY, scanId);
        HttpUtility httpUtility = new HttpUtility();
        return httpUtility.getRequest(getUrl);
    }
}
