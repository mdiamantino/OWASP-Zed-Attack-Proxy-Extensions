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

import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

public class TestUtils {
    public static String buildDomainUrl(String domain) {
        return String.format("https://www.%s.com", domain);
    }

    static String buildResponseHeader(int responseLength) {
        return String.format(
                "HTTP/1.1 200 OK\n"
                        + "Content-Type: text/html; charset=utf-8\n"
                        + "Server: Apache\n"
                        + "Content-Length: %d\n",
                responseLength);
    }

    /**
     * Select at random the rules which will be invalid
     *
     * @param rules Complete set of rules
     * @param msg Som HttpMessage
     * @return Set of rules' names expected to have failed the validity test upon the message
     */
    public static Set<String> buildRandomBehaviourAndGetExpectedResults(
            Set<Rule> rules, HttpMessage msg) {
        Set<String> expectedInvalidRulesNames = new HashSet<>();
        Random random = new Random();
        for (Rule rule : rules) {
            Boolean willHttpMessageBeValidForRule = random.nextBoolean();
            when(rule.isValid(msg)).thenReturn(willHttpMessageBeValidForRule);
            if (!willHttpMessageBeValidForRule) {
                expectedInvalidRulesNames.add(rule.getName());
            }
        }
        return expectedInvalidRulesNames;
    }
}
