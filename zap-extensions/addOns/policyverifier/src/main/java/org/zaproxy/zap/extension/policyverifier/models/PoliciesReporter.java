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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.views.PolicyVerifierPanel;

public class PoliciesReporter {
    private static final Logger logger = Logger.getLogger(PoliciesReporter.class);
    private final List<Policy> policies = new ArrayList<>();
    private boolean isInitializedWithPluginPassiveScanner = false;
    private boolean isTest = false;

    public PoliciesReporter() {}

    private void initialize() {
        if (!isInitializedWithPluginPassiveScanner) {
            PolicyVerifierPassiveScanner.getSingleton().setPoliciesReporter(this);
            isInitializedWithPluginPassiveScanner = true;
        }
    }

    /**
     * Method used to add a policy to the model
     *
     * @param policy Policy to add
     */
    public void addPolicy(Policy policy) {
        if (!isTest) {
            initialize();
        }
        for (Policy previouslyAddedPolicy : policies) {
            if (policy.getName().equals(previouslyAddedPolicy.getName())) {
                policies.remove(previouslyAddedPolicy);
                logger.warn(
                        String.format(
                                "A policy with the same name '%s' was added before, so it is now replaced.",
                                policy.getName()));
                break;
            }
        }
        policies.add(policy);
        logger.info(
                String.format(
                        "Policy %s added. Size of policy list=%d",
                        policy.getName(), policies.size()));
        if (!isTest) {
            PolicyVerifierPanel.getSingleton().updateAndDisplayLoadedPolicies(policies);
        }
    }


    /**
     * Checks for the validity of each policy and delegates the creation of alerts when a Rule is
     * violated
     *
     * @param msg HttpMessage that is checked against policies for validity.
     */
    public String generateReportOnAllPolicies(HttpMessage msg) {
        logger.warn(
                String.format(
                        "Validating HttpMessage against loaded policies : policySize %d",
                        policies.size()));
        StringBuilder strBuilder = new StringBuilder();

        for (Policy policy : policies) {
            logger.info(String.format("inside loop, current policy: %s", policy.getName()));
            Set<String> violatedRulesNames = policy.getViolatedRulesNames(msg);
            String violatedRuleReport = generatePolicyReport(policy.getName(), violatedRulesNames);
            strBuilder.append(String.format("%s", violatedRuleReport));
        }

        String policiesDescription = strBuilder.toString();
        logger.info(String.format("violated policies: %s", policiesDescription));
        return policiesDescription;
    }

    /**
     * Violated rules raise an alert displayed on the UI in the "Alerts" panel with the following
     * description format: "PolicyX.RuleY violated"
     *
     * @param policyName Name of the violated Policy
     * @param violatedRulesNames Set of the violated Policy Rules
     */
    private String generatePolicyReport(String policyName, Set<String> violatedRulesNames) {
        StringBuilder strBuilder = new StringBuilder();
        for (String violatedRule : violatedRulesNames) {
            strBuilder.append(
                    String.format("Policy%s.Rule%s violated; \n", policyName, violatedRule));
        }
        return strBuilder.toString();
    }

    public void setTestMode() {
        this.isTest = true;
    }
}
