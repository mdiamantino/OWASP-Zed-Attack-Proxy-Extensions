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
package org.zaproxy.zap.extension.policyverifier.models;

import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;

public class RuleEnforcingPassiveScannerUnitTest {
    protected RuleEnforcingPassiveScanner rule;

    @BeforeEach
    public void setUp() throws Exception {
        rule = spy(new RuleEnforcingPassiveScanner());
    }

    @Test
    public void shouldDisplayAlertsWhenRulesAreViolated() {
        // Given
        Set<String> rules = new HashSet<>(Arrays.asList("a", "b"));
        Policy policy = mock(Policy.class);
        when(policy.getViolatedRulesNames(isA(HttpMessage.class))).thenReturn(rules);
        when(policy.getName()).thenReturn("policy");

        rule.addPolicy(policy);
        doNothing().when(rule).generateViolatedRuleReport(anyString());

        // When
        rule.scanHttpRequestSend(new HttpMessage(), 1);

        // Then
        verify(rule, times(1)).generateViolatedRuleReport(contains("Policypolicy.Rulea"));
        verify(rule, times(1)).generateViolatedRuleReport(matches(" *Policypolicy\\.Ruleb.*"));
    }
}
