package org.zaproxy.zap.extension.policyverifier;

import org.zaproxy.zap.extension.policyverifier.utils.JarLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PolicyVerifierModel {
    private static PolicyVerifierModel soleModel;
    private final List<Policy> loadedPolicies = new ArrayList<>();

    private PolicyVerifierModel() {
        if (soleModel != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static PolicyVerifierModel getSingleton() {
        if (soleModel == null) {
            soleModel = new PolicyVerifierModel();
        }
        return soleModel;
    }


    public void addPolicy(File jar) {
        JarLoader<Rule> loader = new JarLoader<>();
        Set<Rule> rules = new HashSet<>();
        try {
            rules = loader.loadClassesInJar(jar, Rule.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadedPolicies.add(new Policy(rules));
    }
}
