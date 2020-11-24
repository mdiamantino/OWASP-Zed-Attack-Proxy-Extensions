package org.zaproxy.zap.extension.filetester.model;

import net.sf.json.JSONObject;
import org.zaproxy.zap.extension.filetester.http.HttpUtility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExeFile extends DownloadedFile {

    private static final String API_KEY = "8668d17eb4c599b1abcb850c1f046240c5ce7b42930551905371bb87dd08564b";
    private static final String GET_URL = "https://www.virustotal.com/vtapi/v2/file/report?apikey=%s&resource=%s";
    private static final String POST_URL = "https://www.virustotal.com/vtapi/v2/file/scan";

    private Map<String, String> params;
    private Map<String, InputStream> fileParams;
    private String scanId;
    private boolean virusScanCompleted;
    private List<FileTestResult> virusScans;

    public ExeFile(String name, InputStream file) {
        super(name, file);
        params = new HashMap<>();
        params.put("apikey", API_KEY);
        fileParams = new HashMap<>();
        fileParams.put("file", file);
        virusScans = new LinkedList<>();
    }

    @Override
    public boolean isValid() throws IOException {
        JSONObject postRequest = HttpUtility.postRequest(POST_URL, params, fileParams, this.getName());
        scanId = (String) postRequest.get("scan_id");
        System.out.println(scanId);
        return true;
    }

    @Override
    public List<FileTestResult> getTestResults() {
        if (!virusScanCompleted) {
            try {
                JSONObject results = getScanResults();
                System.out.println(results);
                if (!results.isEmpty()) {
                    int responseCode = (int) results.get("response_code");
                    FileTestResult virusDetection = new FileTestResult("Virus Detection");
                    if (responseCode == 1) {
                        String scanResultMessage = (String) results.get("verbose_msg");
                        int positives = (int) results.get("positives");
                        if (positives > 0) {
                            int total = (int) results.get("total");
                            virusDetection.setResult(true);
                            virusDetection.setRemarks(String.format("%s: %d out of %d scans flagged the file as a virus.", scanResultMessage, positives, total));
                        } else {
                            virusDetection.setResult(false);
                            virusDetection.setRemarks("");
                        }
                        virusScanCompleted = true;
                    } else {
                        // the scan is still queued
                        virusDetection.setRemarks("Virus scan in progress: " + scanId);
                    }
                    virusScans.add(virusDetection);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (virusScanCompleted) {
            isCompleted = true;
        }
        return virusScans;
    }

    private JSONObject  getScanResults() throws IOException {
        String getUrl = String.format(GET_URL, API_KEY, scanId);
        JSONObject  getRequest = HttpUtility.getRequest(getUrl);
        return getRequest;
    }
}
