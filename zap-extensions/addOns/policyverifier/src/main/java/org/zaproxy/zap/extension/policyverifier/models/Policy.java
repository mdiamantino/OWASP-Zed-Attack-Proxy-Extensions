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

import org.apache.log4j.Logger;
import org.parosproxy.paros.network.HttpMessage;

import java.util.HashSet;
import java.util.Set;

/**
 * A Policy is a set of Rules that an HTTP Message must follow, otherwise the policy is violated
 */
public class Policy {
    private final Set<Rule> rules;
    private static final Logger logger = Logger.getLogger(Policy.class);
    private String name;

    public Policy(Set<Rule> rules, String name) {
        this.rules = rules;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Generate a Set of all violated rules' names (not the Rule objects themselves otherwise does
     * not follow Low of Demeter)
     *
     * @param msg Http Message to check against the rules defining this Policy
     * @return A set the names of the violated policies
     */
    public Set<String> getViolatedRulesNames(HttpMessage msg) {
        Set<String> violatedRulesNames = new HashSet<>();
        logger.warn("Inside getViolatedRulesNames");
        for (Rule rule : rules) {
            logger.warn(
                    String.format(
                            "Checking if rule %s is valid : %b",
                            rule.getName(), rule.isValid(msg)));
            if (!rule.isValid(msg)) {
                violatedRulesNames.add(rule.getName());
            }
        }
        return violatedRulesNames;
    }

    // For test purposes
    public Set<Rule> getRules() {
        return rules;
    }
}
