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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;

public class PoliciesReporterUnitTest {

    @Test
    public void generateReportWithAPolicy() {
        PoliciesReporter reporter = new PoliciesReporter();
        reporter.setTestMode();
        Policy policy = mock(Policy.class);
        HttpMessage msg = new HttpMessage();
        Set<String> violatedRulesNames = new HashSet<>();
        String ruleName = RandomStringUtils.randomAlphabetic(3);
        String policyName = RandomStringUtils.randomAlphabetic(3);
        String policiesDescription = "Policy%s.Rule%s violated;";
        violatedRulesNames.add(ruleName);
        given(policy.getViolatedRulesNames(msg)).willReturn(violatedRulesNames);
        given(policy.getName()).willReturn(policyName);
        reporter.addPolicy(policy);
        reporter.addPolicy(policy);
        assertEquals(
                String.format(policiesDescription, policyName, ruleName),
                reporter.generateReportOnAllPolicies(msg).trim());
    }

    @Test
    public void generateReportWithoutAPolicy() {
        PoliciesReporter reporter = new PoliciesReporter();
        assertEquals("", reporter.generateReportOnAllPolicies(new HttpMessage()).trim());
    }
}
