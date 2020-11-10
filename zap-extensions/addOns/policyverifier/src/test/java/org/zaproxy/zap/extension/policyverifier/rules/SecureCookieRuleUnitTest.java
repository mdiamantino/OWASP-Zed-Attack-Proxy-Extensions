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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

public class SecureCookieRuleUnitTest {
    SecureCookie rule;
    private List<String> flags;

    @BeforeEach
    public void setup() {
        rule = new SecureCookie();
        flags = new ArrayList<>(Arrays.asList("Secure", "HttpOnly", "SameSite"));
    }

    @Test
    public void isValid_CookiesContainAllTheFlags_Valid() throws HttpMalformedHeaderException {
        HttpMessage msg = constructHttpMessage(flags);
        System.out.println(msg.getResponseHeader());
        assertTrue(rule.isValid(msg));
    }

    @Test
    public void isValid_CookiesContainOnlySomeOfValidFlags_NotValid()
            throws HttpMalformedHeaderException {
        Random rand = new Random();
        flags.remove(rand.nextInt(flags.size()));
        List<String> cookieParams = new ArrayList<>(flags);
        HttpMessage msg = constructHttpMessage(cookieParams);
        assertFalse(rule.isValid(msg));
    }

    @Test
    public void isValid_EmptyCookieParameters_NotValid() throws HttpMalformedHeaderException {
        HttpMessage msg = constructHttpMessage(new ArrayList<>());
        assertFalse(rule.isValid(msg));
    }

    @Test
    public void isValid_NoCookiesAtAll_Valid() {
        HttpMessage msg = new HttpMessage();
        assertTrue(rule.isValid(msg));
    }

    private HttpMessage constructHttpMessage(List<String> cookieParamsToInclude)
            throws HttpMalformedHeaderException {
        HttpMessage msg = new HttpMessage();
        msg.setRequestHeader("GET https://www.example.com/test/ HTTP/1.1");

        String cookieParams = String.join(";", cookieParamsToInclude);
        msg.setResponseBody("<html></html>");
        msg.setResponseHeader(
                "HTTP/1.1 200 OK\r\n"
                        + "Server: Apache-Coyote/1.1\r\n"
                        + "Set-Cookie: test=123; Path=/; "
                        + cookieParams
                        + "\r\n"
                        + "Content-Type: text/html;charset=ISO-8859-1\r\n"
                        + "Content-Length: "
                        + msg.getResponseBody().length()
                        + "\r\n");
        return msg;
    }
}
