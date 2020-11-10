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
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;

public class NoEmailsUnitTest {
    private NoEmails rule;

    @BeforeEach
    public void setUp() throws Exception {
        rule = new NoEmails();
    }

    private HttpMessage getMockMessage(String headers, String body) {
        HttpMessage msg = mock(HttpMessage.class, RETURNS_DEEP_STUBS);
        when(msg.getRequestHeader().isText()).thenReturn(true);
        when(msg.getRequestHeader().getHeadersAsString()).thenReturn(headers);
        when(msg.getRequestBody().toString()).thenReturn(body);
        return msg;
    }

    @Test
    public void shouldNotFlagRequestWithoutEmail() {
        HttpMessage msg = getMockMessage("", "username=example");
        assertTrue(rule.isValid(msg));
    }

    @Test
    public void shouldFlagPostRequestWithEmail() {
        HttpMessage msg = getMockMessage("", "email=example@example.com");
        assertFalse(rule.isValid(msg));
    }

    @Test
    public void shouldFlagPostGetRequestWithEmail() {
        HttpMessage msg = getMockMessage("GET /?email=example@example.com HTTP/1.1\n", "");
        assertFalse(rule.isValid(msg));
    }
}
