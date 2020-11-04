package org.zaproxy.zap.extension.policyverifier;

import net.htmlparser.jericho.Source;
import org.apache.log4j.Logger;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.utils.JarLoader;
import org.zaproxy.zap.extension.pscan.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RuleEnforcingPassiveScanner extends PluginPassiveScanner {
    private static RuleEnforcingPassiveScanner soleModel;
//    private boolean isEnabled = false;
    private final List<Policy> policies = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(RuleEnforcingPassiveScanner.class);

    @Override
    public int getPluginId() {
        return 4830;
    }

    public RuleEnforcingPassiveScanner() {
        logger.warn("Starting Rule Enforcing Passive scanner");
        if (soleModel != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.setEnabled(true);
    }

    public static RuleEnforcingPassiveScanner getSingleton() {
        if (soleModel == null) {
            soleModel = new RuleEnforcingPassiveScanner();
        }
        return soleModel;
    }

    public void addPolicy(File jar) {
        logger.warn("---Adding Policy");
        JarLoader<Rule> loader = new JarLoader<>();
        Set<Rule> rules = new HashSet<>();
        try {
            rules = loader.loadClassesInJar(jar, Rule.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.warn("---Got Rules");
        String policyName = jar.getName();
        Policy loadedPolicy = new Policy(rules, policyName);
        policies.add(loadedPolicy);
    }

    private void verifyPoliciesOverMsg(HttpMessage msg) {
        logger.warn("Validating Message");
        for (Policy policy : policies) {
            policy.validateRules(msg);
        }
    }

    @Override
    public void scanHttpRequestSend(HttpMessage msg, int id) {
        verifyPoliciesOverMsg(msg);
    }

    @Override
    public void scanHttpResponseReceive(HttpMessage msg, int id, Source source) {
        logger.warn("Received message");
        verifyPoliciesOverMsg(msg);
    }

    @Override
    public void setParent(PassiveScanThread parent) {
        // Nothing
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setEnabled(boolean enabled) {
//        isEnabled = enabled;
        if (enabled) {
            Control.getSingleton()
                    .getExtensionLoader()
                    .getExtension(ExtensionPassiveScan.class).addPassiveScanner(this);
        }
    }

}
