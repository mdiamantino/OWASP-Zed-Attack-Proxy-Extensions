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

public class RuleEnforcingPassiveScannerUnitTest {
    protected PolicyVerifierPassiveScanner reps;
    /*
       @BeforeEach
       public void setUp() throws Exception {
           reps = spy(new PolicyVerifierPassiveScanner());
           // We cannot raise real alerts, because the ZAP machinery is never setup
           doNothing().when(reps).generateViolatedRuleReport(anyString());
       }

       private Policy createPolicyMock(String name, List<String> ruleNames) {
           Set<String> rules = new HashSet<>(ruleNames);
           Policy policy = mock(Policy.class);
           when(policy.getViolatedRulesNames(isA(HttpMessage.class))).thenReturn(rules);
           when(policy.getName()).thenReturn(name);
           return policy;
       }

       @Test
       public void shouldDisplayAlertsWhenRulesAreViolated() {
           // Given
           reps.addPolicy(createPolicyMock("policy", Arrays.asList("a", "b")));

           // When
           reps.scanHttpRequestSend(new HttpMessage(), 1);

           // Then
           verify(reps, times(1))
                   .generateViolatedRuleReport(
                           matches(".*Policypolicy\\.Rulea[\\S\\s]*Policypolicy\\.Ruleb[\\S\\s]*"));
       }

       @Test
       public void shouldOnlyKeepNewerDuplicatePolicy() {
           // Given
           reps.addPolicy(createPolicyMock("policy", Arrays.asList("a", "b")));
           reps.addPolicy(createPolicyMock("policy", Arrays.asList("c", "d")));

           // When
           reps.scanHttpRequestSend(new HttpMessage(), 1);

           // Then
           verify(reps, never()).generateViolatedRuleReport(contains("Policypolicy.Rulea"));
           verify(reps, never()).generateViolatedRuleReport(matches(" *Policypolicy\\.Ruleb.*"));
           verify(reps, times(1)).generateViolatedRuleReport(contains("Policypolicy.Rulec"));
       }

    */
}
