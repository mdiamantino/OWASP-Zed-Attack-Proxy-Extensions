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

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.jarRules.TestUtils;

class PolicyUnitTest {
    private Policy policy;

    @AfterEach
    public void setUp() throws Exception {
        policy = null;
    }

    @Test
    void getViolatedRulesNames() {
        Set<Rule> rules = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            rules.add(Mockito.mock(Rule.class));
        }

        HttpMessage msg = new HttpMessage();
        Set<String> expectedInvalidRulesNames =
                TestUtils.buildRandomBehaviourAndGetExpectedResults(rules, msg);

        String POLICY_NAME_FOR_TEST = "PolicyExample";
        policy = new Policy(rules, POLICY_NAME_FOR_TEST);
        Set<String> invalidRules = policy.getViolatedRulesNames(msg);
        assertEquals(expectedInvalidRulesNames, invalidRules);
    }
}
