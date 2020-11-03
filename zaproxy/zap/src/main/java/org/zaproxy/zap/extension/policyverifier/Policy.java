package org.zaproxy.zap.extension.policyverifier;

import java.util.Set;

public class Policy {
    private final Set<Rule> policyRules;

    public Policy(Set<Rule> policyRules) {
        this.policyRules = policyRules;
    }

    public Set<Rule> getPolicyRules() {
        return policyRules;
    }
}
