package org.zaproxy.zap.extension.policyverifier.models;

import org.parosproxy.paros.network.HttpMessage;

import java.util.HashSet;
import java.util.Set;

/**
 * A Policy is a set of Rules that an HTTP Message must follow, otherwise the policy is violated
 */
public class Policy {
    private final Set<Rule> rules;
    private String name;

    public Policy(Set<Rule> rules, String name) {
        this.rules = rules;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Generate a Set of all violated rules' names
     * (not the Rule objects themselves otherwise does not follow Low of Demeter)
     *
     * @param msg Http Message to check against the rules defining this Policy
     * @return A set the names of the violated policies
     */
    public Set<String> getViolatedRulesNames(HttpMessage msg) {
        Set<String> violatedRulesNames = new HashSet<>();
        for (Rule rule : rules) {
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