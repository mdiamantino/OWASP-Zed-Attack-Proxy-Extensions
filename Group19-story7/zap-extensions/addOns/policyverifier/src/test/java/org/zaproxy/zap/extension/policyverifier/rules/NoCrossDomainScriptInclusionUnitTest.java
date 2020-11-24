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
package org.zaproxy.zap.extension.policyverifier.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

public class NoCrossDomainScriptInclusionUnitTest extends org.zaproxy.zap.testutils.TestUtils {
    NoCrossDomainScriptInclusion rule;
    int LENGTH_OF_RANDOM_DOMAINS = 10;

    @BeforeEach
    public void setup() throws Exception {
        setUpZap();
        rule = new NoCrossDomainScriptInclusion();
    }

    private String getHTMLWithDomainScript(String domain) {
        return String.format(
                "<html><head><script src=%s /></head></html>", TestUtils.buildDomainUrl(domain));
    }

    private HttpMessage generateHttpMessageWithVariableCrossDomainScripts(
            boolean isCrossDomainScript) throws HttpMalformedHeaderException {
        HttpMessage message = new HttpMessage();
        String originalDomain = RandomStringUtils.randomAlphabetic(LENGTH_OF_RANDOM_DOMAINS);
        String url = TestUtils.buildDomainUrl(originalDomain);
        message.setRequestHeader("GET " + url + " HTTP/1.1");
        String htmlResponseBody;
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

    private HttpMessage buildHttpMessage(String htmlResponseBody)
            throws HttpMalformedHeaderException {
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
        HttpMessage msg =
                buildHttpMessage("<html><head><script src=/someDir/someScript.js /></head></html>");
        assertTrue(rule.isValid(msg));
    }

    @Test
    public void isValid_DifferentDomainScript_NotValid() throws HttpMalformedHeaderException {
        HttpMessage msg = generateHttpMessageWithVariableCrossDomainScripts(true);
        assertFalse(rule.isValid(msg));
    }

    @Test
    public void isValid_noScriptInResponse_Valid() throws HttpMalformedHeaderException {
        HttpMessage message =
                buildHttpMessage(
                        "<html><head><p>This page does not contain scripts</p></head></html>");
        assertTrue(rule.isValid(message));
    }

    @Test
    public void isValid_emptyHtml_Valid() throws HttpMalformedHeaderException {
        HttpMessage message = buildHttpMessage("");
        assertTrue(rule.isValid(message));
    }

    @Test
    public void getName_isActualName_True() {
        assertEquals(rule.getName(), "NoCrossDomainScriptInclusion");
    }
}
