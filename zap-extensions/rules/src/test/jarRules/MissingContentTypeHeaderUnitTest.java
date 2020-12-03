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
package org.zaproxy.zap.extension.policyverifier.models.jarRules;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

public class MissingContentTypeHeaderUnitTest {
    NoMissingContentTypeHeader rule;

    private HttpMessage generateHttpMessage() throws HttpMalformedHeaderException {
        HttpMessage message = new HttpMessage();
        String url = TestUtils.buildDomainUrl("domain-example");
        message.setRequestHeader("GET " + url + " HTTP/1.1");
        String responseBody = "<html></html>";
        message.setResponseBody(responseBody);
        return message;
    }

    @BeforeEach
    public void setup() {
        rule = new NoMissingContentTypeHeader();
    }

    @Test
    public void isValid_ContentTypeHeaderPresent_Valid() throws HttpMalformedHeaderException {
        HttpMessage msg = generateHttpMessage();
        msg.setResponseHeader(
                String.format(
                        "HTTP/1.1 200 OK\n"
                                + "Content-Type: text/html; charset=utf-8\n"
                                + "Server: Apache\n"
                                + "Content-Length: %d\n",
                        msg.getResponseBody().toString().length()));
        assertTrue(rule.isValid(msg));
    }

    @Test
    public void isValid_ContentTypeHeaderPresentAndEmpty_NotValid()
            throws HttpMalformedHeaderException {
        HttpMessage msg = generateHttpMessage();
        msg.setResponseHeader(
                String.format(
                        "HTTP/1.1 200 OK\n"
                                + "Content-Type: \n"
                                + "Server: Apache\n"
                                + "Content-Length: %d\n",
                        msg.getResponseBody().toString().length()));
        assertFalse(rule.isValid(msg));
    }

    @Test
    public void isValid_ContentTypeHeaderNotThere_NotValid() throws HttpMalformedHeaderException {
        HttpMessage msg = generateHttpMessage();
        msg.setResponseHeader(
                String.format(
                        "HTTP/1.1 200 OK\n" + "Server: Apache\n" + "Content-Length: %d\n",
                        msg.getResponseBody().toString().length()));
        assertFalse(rule.isValid(msg));
    }

    @Test
    public void isValid_NoBody_Valid() throws HttpMalformedHeaderException {
        HttpMessage msg = generateHttpMessage();
        msg.setResponseBody("");
        msg.setResponseHeader(
                String.format(
                        "HTTP/1.1 200 OK\n"
                                + "Content-Type: \n"
                                + "Server: Apache\n"
                                + "Content-Length: %d\n",
                        msg.getResponseBody().toString().length()));
        assertTrue(rule.isValid(msg));
    }
}
