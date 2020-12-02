package org.zaproxy.zap.extension.policyverifier.models;

import org.junit.jupiter.api.Test;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.OptionsParam;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.testutils.PassiveScannerTestUtils;
import org.zaproxy.zap.utils.ZapXmlConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class PolicyVerifierPassiveScannerTest extends PassiveScannerTestUtils<PolicyVerifierPassiveScanner> {
    private Model model;

    @Override
    protected PolicyVerifierPassiveScanner createScanner() {
        scanner = new PolicyVerifierPassiveScanner();
        // Mock the model and options
        model = mock(Model.class, withSettings().lenient());
        OptionsParam options = new OptionsParam();
        ZapXmlConfiguration conf = new ZapXmlConfiguration();
        options.load(conf);
        when(model.getOptionsParam()).thenReturn(options);
        scanner.setModel(model);
        return scanner;
    }

    @Test
    void getSingleton() {
        // TODO
    }

    @Test
    void getPluginId() {
        assertEquals(5000019, scanner.getPluginId());
    }

    @Test
    void generateViolatedRuleReport() throws HttpMalformedHeaderException {
        PoliciesReporter policyReporter = mock(PoliciesReporter.class);
        HttpMessage msg = new HttpMessage();
        msg.setRequestHeader("GET https://www.example.com/test/ HTTP/1.1");

        msg.setResponseBody("<html></html>");
        msg.setResponseHeader(
                "HTTP/1.1 200 OK\r\n"
                        + "Server: Apache-Coyote/1.1\r\n"
                        + "Set-Cookie: test=123; Path=/; HttpOnly\r\n"
                        + "Content-Type: text/html;charset=ISO-8859-1\r\n"
                        + "Content-Length: "
                        + msg.getResponseBody().length()
                        + "\r\n");
        String report = "TOPRINT";
//        when(policyReporter.generateReportOnAllPolicies(msg)).thenReturn(report);
        // scanHttpResponseReceive(msg);
        // TODO
    }

    @Test
    void scanHttpRequestSend() {
        // TODO
    }

    @Test
    void setParent() {
        // TODO
    }

    @Test
    void getName() {
        assertEquals("!policyverifierpluginpassivescannername!", scanner.getName());
    }


}