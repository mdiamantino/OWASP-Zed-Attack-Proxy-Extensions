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
package org.zaproxy.zap.extension.filetester.models.httpUtils;

import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * HttpUtility manages the interaction with external API's.
 */
public class HttpUtility {

    private static final String boundary = "*****";
    private static final String crlf = "\r\n";
    private static final String twoHyphens = "--";
    private static String requestURL;
    private HttpURLConnection httpConn;
    private DataOutputStream request;

    /**
     * Performs HTTP GET request on an external API.
     *
     * @param URL The URL of the external API.
     * @return The response from the external API in JSONObject.
     * @throws IOException if cannot read the input stream.
     */
    public JSONObject getRequest(String URL) throws IOException {
        requestURL = URL;
        constructGetRequest();
        return readMultipleLinesResponse();
    }

    /**
     * Performs HTTP POST request on an external API.
     *
     * @param URL        The URL of the external API.
     * @param params     The parameters of the POST request.
     * @param fileParams The file parameters of the POST request.
     * @param fileName   The name of the file.
     * @return The response from the external API in JSONObject.
     * @throws IOException if cannot read the input stream.
     */
    public JSONObject postRequest(String URL, Map<String, String> params, Map<String, InputStream> fileParams,
                                  String fileName) throws IOException {
        requestURL = URL;
        JSONObject response;
        try {
            constructPostRequest(params, fileParams, fileName);
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
            request.flush();
            request.close();
            response = readMultipleLinesResponse();
        } finally {
            disconnect();
        }
        return response;
    }

    /**
     * Sets up the HTTP GET request.
     *
     * @throws IOException if cannot read the input stream.
     */
    private void constructGetRequest() throws IOException {
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoInput(true);
        httpConn.setDoOutput(false);
    }

    /**
     * Sets up the HTTP POST request.
     *
     * @param params     The parameters of the POST request.
     * @param fileParams The file parameters of the POST request.
     * @param fileName   The name of the file.
     * @throws IOException if cannot read the input stream.
     */
    private void constructPostRequest(Map<String, String> params, Map<String, InputStream> fileParams,
                                      String fileName) throws IOException {
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Cache-Control", "no-cache");
        httpConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        request = new DataOutputStream(httpConn.getOutputStream());
        for (Map.Entry<String, String> p : params.entrySet()) {
            addFormField(p.getKey(), p.getValue());
        }
        for (Map.Entry<String, InputStream> f : fileParams.entrySet()) {
            addFilePart(f.getKey(), f.getValue(), fileName);
        }
    }

    /**
     * Adds form fields of type text/plain to the HTTP POST request.
     *
     * @param fieldName  The name of the field.
     * @param fieldValue The value of the field.
     * @throws IOException if cannot read the input stream.
     */
    private void addFormField(String fieldName, String fieldValue) throws IOException {
        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"" + crlf);
        request.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
        request.writeBytes(crlf);
        request.writeBytes(fieldValue + crlf);
        request.flush();
    }

    /**
     * Adds form fields of file in bytes array to the HTTP POST request.
     *
     * @param fieldName  The name of the field.
     * @param fileStream The input stream of the file.
     * @param fileName   The name of the file.
     * @throws IOException if cannot read the input stream.
     */
    private void addFilePart(String fieldName, InputStream fileStream, String fileName) throws IOException {
        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes(
                "Content-Disposition: form-data; name=\"" + fieldName + "\";filename=\"" + fileName + "\"" + crlf);
        request.writeBytes(crlf);
        byte[] bytes = IOUtils.toByteArray(fileStream);
        request.write(bytes);
    }

    /**
     * Reads the response from the HTTP request.
     *
     * @return The response from the HTTP request in JSONObject format.
     * @throws IOException if cannot read the input stream.
     */
    private JSONObject readMultipleLinesResponse() throws IOException {
        String response = "{}";
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            InputStream responseStream = new BufferedInputStream(httpConn.getInputStream());
            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();
            response = stringBuilder.toString();
        }
        return JSONObject.fromObject(response);
    }

    /**
     * Disconnects the HTTP connection if a connection exists.
     */
    private void disconnect() {
        if (httpConn != null) {
            httpConn.disconnect();
        }
    }
}
