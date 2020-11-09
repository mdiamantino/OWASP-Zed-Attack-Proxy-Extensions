package org.zaproxy.zap.extension.policyverifier.rules;

import org.apache.commons.httpclient.URIException;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class HttpsRuleUnitTest {

    HttpsOnly rule = new HttpsOnly();

    @Test
    public void isValid_IsSecureTraffic_Valid() throws URIException, HttpMalformedHeaderException {
        HttpMessage message = new HttpMessage();
        String url = TestUtils.buildDomainUrl("secure-domain-example");
        message.setRequestHeader("GET " + url + " HTTP/1.1");
        String responseBody = "<html></html>";
        message.setRequestBody(responseBody);
        message.getRequestHeader().setSecure(true);
        assertTrue(rule.isValid(message));
    }


    @Test
    public void isValid_IsNotSecureTraffic_NotValid() throws URIException, HttpMalformedHeaderException {
        HttpMessage message = new HttpMessage();
        message.setRequestHeader("GET " + "http://insecure-domain-example.com" + " HTTP/1.1");
        String responseBody = "<html></html>";
        message.setRequestBody(responseBody);
        message.getRequestHeader().setSecure(false);
        assertFalse(rule.isValid(message));
    }
}
