package org.zaproxy.zap.extension.filetester.http;

import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;

public class HttpUtility {

    private static HttpURLConnection httpConn;
    private static DataOutputStream request;
    private static final String boundary = "*****";
    private static final String crlf = "\r\n";
    private static final String twoHyphens = "--";
    private static String requestURL;

    public static Map<String, String > getRequest(String URL) throws IOException {
        requestURL = URL;
        Map<String , String> response = null;
        try {
            constructGetRequest();
            response = readMultipleLinesRespone();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
        return response;
    }

    private static void constructGetRequest() throws IOException {
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoInput(true); // true if we want to read server's response
        httpConn.setDoOutput(false); // false indicates this is a GET request
    }

    private static Map<String , String > readMultipleLinesRespone() throws IOException {
        String response = "";

        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            InputStream responseStream = new BufferedInputStream(httpConn.getInputStream());

            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            response = stringBuilder.toString();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
        return JSONObject.fromObject(response);
    }

    private static void disconnect() {
        if (httpConn != null) {
            httpConn.disconnect();
        }
    }

    private static void addFormField(String name, String value) throws IOException {
        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + crlf);
        request.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
        request.writeBytes(crlf);
        request.writeBytes(value + crlf);
        request.flush();
    }

    private static void addFilePart(String fieldName, File uploadFile) throws IOException {
        String fileName = uploadFile.getName();
        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes(
                "Content-Disposition: form-data; name=\"" + fieldName + "\";filename=\"" + fileName + "\"" + crlf);
        request.writeBytes(crlf);
        byte[] bytes = Files.readAllBytes(uploadFile.toPath());
        request.write(bytes);
    }

    private static void constructPostRequest(Map<String, String> params, Map<String, File> fileParams)
            throws IOException {
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Cache-Control", "no-cache");
        httpConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        request = new DataOutputStream(httpConn.getOutputStream());
        for (Map.Entry<String, String> p : params.entrySet()) {
            addFormField(p.getKey(), p.getValue());
        }
        for (Map.Entry<String, File> f : fileParams.entrySet()) {
            addFilePart(f.getKey(), f.getValue());
        }
    }

    public static Map<?, ?> postRequest(String URL, Map<String, String> params, Map<String, File> fileParams)
            throws IOException {
        requestURL = URL;
        Map<?, ?> response = null;
        try {
            constructPostRequest(params, fileParams);
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
            request.flush();
            request.close();
            response = readMultipleLinesRespone();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
        return response;
    }
}
