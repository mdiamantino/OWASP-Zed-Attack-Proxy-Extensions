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
import static org.mockito.Mockito.*;

import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;

/**
 * Unit test for {@link org.zaproxy.zap.extension.policyverifier.models.jarRules.NoBannedDomains}.
 */
public class NoBannedDomainsUnitTest {
    private NoBannedDomains rule;
    private HttpMessage msg;

    @BeforeEach
    public void setUp() throws Exception {
        rule = new NoBannedDomains();
        msg = mock(HttpMessage.class);
    }

    private HttpMessage getMockMessage(String hostname) {
        HttpMessage msg = mock(HttpMessage.class, RETURNS_DEEP_STUBS);
        when(msg.getRequestHeader().getHostName()).thenReturn(hostname);
        return msg;
    }

    @Test
    public void isValid_DomainNotInList_Valid() {
        HttpMessage msg = getMockMessage("this-domain-is-not-in-list.com");
        assertTrue(rule.isValid(msg));
    }

    @Test
    public void isValid_DomainIsInList_NotValid() {
        HttpMessage msg = getMockMessage(getRandomHostInList());
        assertFalse(rule.isValid(msg));
    }

    private String getRandomHostInList() {
        Random rand = new Random();
        int ransomIndex = rand.nextInt(rule.getBANNED_DOMAINS().size());
        return rule.getBANNED_DOMAINS().get(ransomIndex);
    }

    @Test
    public void isValid_SubDomainOfDomainInList_NotValid() {
        HttpMessage msg = getMockMessage("www." + getRandomHostInList());
        assertFalse(rule.isValid(msg));
    }
}
