package org.zaproxy.zap.extension.policyverifier;

import org.parosproxy.paros.control.Control;
import org.zaproxy.zap.extension.pscan.ExtensionPassiveScan;

import java.util.Set;

public class Policy {
    private final Set<Rule> policyRules;

    public Policy(Set<Rule> policyRules) {
        this.policyRules = policyRules;
    }

    public void enable() {
        System.out.println("---Enabling");
        for (Rule rule : policyRules){
            Control.getSingleton()
                    .getExtensionLoader()
                    .getExtension(ExtensionPassiveScan.class).addPassiveScanner(rule);
        }
    }

}
