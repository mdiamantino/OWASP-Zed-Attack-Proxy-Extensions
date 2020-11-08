package org.zaproxy.zap.extension.policyverifier.rules;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.policies.CrossDomainScriptInclusion;

import static org.junit.jupiter.api.Assertions.*;

public class CrossDomainScriptInclusionUnitTest {
    CrossDomainScriptInclusion rule;
    int LENGTH_OF_RANDOM_DOMAINS = 10;

    @BeforeEach
    public void setup() {
        rule = new CrossDomainScriptInclusion();
    }

    private String getHTMLWithDomainScript(String domain) {
        return String.format("<html><head><script src=%s /></head></html>", TestUtils.buildDomainUrl(domain));
    }

    private HttpMessage generateHttpMessageWithVariableCrossDomainScripts(boolean isCrossDomainScript) throws HttpMalformedHeaderException {
        HttpMessage message = new HttpMessage();
        String originalDomain = RandomStringUtils.randomAlphabetic(LENGTH_OF_RANDOM_DOMAINS);
        String url = TestUtils.buildDomainUrl(originalDomain);
        message.setRequestHeader("GET " + url + " HTTP/1.1");
        String htmlResponseBody = "";
        if (!isCrossDomainScript) {
            htmlResponseBody = getHTMLWithDomainScript(originalDomain);
        } else {
            String differentDomain = RandomStringUtils.randomAlphabetic(LENGTH_OF_RANDOM_DOMAINS);
            htmlResponseBody = getHTMLWithDomainScript(differentDomain);
        }
        message.setResponseHeader(TestUtils.buildResponseHeader(htmlResponseBody.length()));
        message.setResponseBody(htmlResponseBody);
        return message;
    }

    private HttpMessage getHttpMessage(String htmlResponseBody) throws HttpMalformedHeaderException {
        HttpMessage message = new HttpMessage();
        message.setRequestHeader("GET " + TestUtils.buildDomainUrl("something") + " HTTP/1.1");
        message.setResponseBody(htmlResponseBody);
        message.setResponseHeader(TestUtils.buildResponseHeader(htmlResponseBody.length()));
        return message;
    }

    @Test
    public void isValid_SameDomainScriptByUrl_Valid() throws HttpMalformedHeaderException {
        HttpMessage msg = generateHttpMessageWithVariableCrossDomainScripts(false);
        assertTrue(rule.isValid(msg));
    }

    @Test
    public void isValid_SameDomainScriptByPath_Valid() throws HttpMalformedHeaderException {
        HttpMessage msg = getHttpMessage("<html><head><script src=/someDir/someScript.js /></head></html>");
        assertTrue(rule.isValid(msg));
    }

    @Test
    public void isValid_DifferentDomainScript_NotValid() throws HttpMalformedHeaderException {
        HttpMessage msg = generateHttpMessageWithVariableCrossDomainScripts(true);
        assertFalse(rule.isValid(msg));
    }

    @Test
    public void isValid_noScriptInResponse_Valid() throws HttpMalformedHeaderException {
        HttpMessage message = getHttpMessage("<html><head><p>This page does not contain scripts</p></head></html>");
        assertTrue(rule.isValid(message));
    }

    @Test
    public void isValid_emptyHtml_Valid() throws HttpMalformedHeaderException {
        HttpMessage message = getHttpMessage("");
        assertTrue(rule.isValid(message));
    }

    @Test
    public void getName_isActualName_True() {
        assertEquals(rule.getName(), "CrossDomainScriptInclusion");
    }


}
