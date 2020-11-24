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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.httpclient.URIException;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

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
    public void isValid_IsNotSecureTraffic_NotValid()
            throws URIException, HttpMalformedHeaderException {
        HttpMessage message = new HttpMessage();
        message.setRequestHeader("GET " + "http://insecure-domain-example.com" + " HTTP/1.1");
        String responseBody = "<html></html>";
        message.setRequestBody(responseBody);
        message.getRequestHeader().setSecure(false);
        assertFalse(rule.isValid(message));
    }
}
