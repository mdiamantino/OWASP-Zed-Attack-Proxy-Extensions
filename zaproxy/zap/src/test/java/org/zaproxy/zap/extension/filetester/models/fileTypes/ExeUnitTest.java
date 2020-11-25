package org.zaproxy.zap.extension.filetester.models.fileTypes;

import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.zaproxy.zap.extension.filetester.models.FileTestResult;
import org.zaproxy.zap.extension.filetester.models.FileTesterUnitTestHelper;
import org.zaproxy.zap.extension.filetester.models.httpUtils.HttpUtility;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ExeUnitTest {

    private static final String API_KEY = "8668d17eb4c599b1abcb850c1f046240c5ce7b42930551905371bb87dd08564b";
    private static final String GET_URL = "https://www.virustotal.com/vtapi/v2/file/report?apikey=%s&resource=%s";
    private static final String POST_URL = "https://www.virustotal.com/vtapi/v2/file/scan";

    private static ExeFile createExeForPost(String name, String mockResponse) throws IOException {
        ExeFile exeFile = (ExeFile) FileTesterUnitTestHelper.createFile(name);
        HttpUtility httpMock = mock(HttpUtility.class);
        Map<String, String> mockParams = new HashMap<>();
        mockParams.put("apikey", API_KEY);
        Map<String, InputStream> mockFileParams = new HashMap<>();
        mockFileParams.put("file", exeFile.getFile());
        JSONObject mockJson = JSONObject.fromObject(mockResponse);
        given(httpMock.postRequest(POST_URL, mockParams, mockFileParams, exeFile.getName())).willReturn(mockJson);
        exeFile.setHttpUtility(httpMock);
        return exeFile;
    }

    private static ExeFile createExeForGet(String name, String mockResponse) throws IOException {
        ExeFile exeFile = (ExeFile) FileTesterUnitTestHelper.createFile(name);
        HttpUtility httpMock = mock(HttpUtility.class);
        String url = String.format(GET_URL, API_KEY, null);
        JSONObject mockJson = JSONObject.fromObject(mockResponse);
        given(httpMock.getRequest(url)).willReturn(mockJson);
        exeFile.setHttpUtility(httpMock);
        return exeFile;
    }

    @Test
    public void exeFileSentForScanning() throws IOException {
        String mockPostResponse = "{\n" +
                " 'response_code': 1,\n" +
                " 'scan_id': '1234567890',\n" +
                " 'verbose_msg': 'Scan request successfully queued, come back later for the report',\n" +
                "}";
        ExeFile exeFile = createExeForPost("exeTestSample.exe", mockPostResponse);
        assertTrue(exeFile.isValid());
        assertFalse(exeFile.isCompleted());
    }

    @Test
    public void exeFileWithoutVirus() throws IOException {
        String mockGetResponse = "{\n" +
                "   \"response_code\":1,\n" +
                "   \"verbose_msg\":\"Scan finished, scan information embedded in this object\",\n" +
                "   \"scan_id\":\"1234567890\",\n" +
                "   \"positives\":0,\n" +
                "   \"total\":40\n" +
                "}";
        ExeFile exeFile = createExeForGet("exeTestSample.exe", mockGetResponse);
        List<FileTestResult> results = exeFile.getTestResults();
        assertFalse(results.get(0).getResult());
        assertTrue(exeFile.isCompleted());
    }

    @Test
    public void exeFileWithVirus() throws IOException {
        String mockGetResponse = "{\n" +
                "   \"response_code\":1,\n" +
                "   \"verbose_msg\":\"Scan finished, scan information embedded in this object\",\n" +
                "   \"scan_id\":\"1234567890\",\n" +
                "   \"positives\":40,\n" +
                "   \"total\":40\n" +
                "}";
        ExeFile exeFile = createExeForGet("exeTestSample.exe", mockGetResponse);
        List<FileTestResult> results = exeFile.getTestResults();
        assertTrue(results.get(0).getResult());
        assertTrue(exeFile.isCompleted());
    }

    @Test
    public void exeFileScanningInProgress() throws IOException {
        String mockGetResponse = "{\n" +
                "   \"response_code\":-2,\n" +
                "   \"scan_id\":\"1234567890\",\n" +
                "   \"verbose_msg\":\"Your resource is queued for analysis\"\n" +
                "}";
        ExeFile exeFile = createExeForGet("exeTestSample.exe", mockGetResponse);
        assertEquals(1, exeFile.getTestResults().size());
        assertFalse(exeFile.isCompleted());
    }
}
