package org.zaproxy.zap.extension.policyverifier;

import org.parosproxy.paros.network.HttpMessage;

import java.util.Set;

public class Policy {
    private final Set<Rule> rules;
    private final String name;

    public Policy(Set<Rule> rules, String name) {
        this.rules = rules;
        this.name = name;
    }

    public void validateRules(HttpMessage msg) {
        for (Rule rule : rules) {
            if (!rule.isValid(msg)) {
                System.out.println("Policy: " + name + ", Rule :" + rule.getName());
            }
        }
    }


}
