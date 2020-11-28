package org.zaproxy.zap.extension.policyverifier.models;

import org.apache.log4j.Logger;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.views.PolicyVerifierPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PoliciesReporter {
    private static final Logger logger = Logger.getLogger(PoliciesReporter.class);
    private List<Policy> policies = new ArrayList<>();

    public PoliciesReporter() {
        PolicyVerifierPassiveScanner.getSingleton().setPoliciesReporter(this);
    }

    /**
     * Method used to add a policy to the model
     *
     * @param policy Policy to add
     */
    public void addPolicy(Policy policy) {
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
        PolicyVerifierPanel.getSingleton().updateAndDisplayLoadedPolicies(policies);
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
     * @param policyName         Name of the violated Policy
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
}
