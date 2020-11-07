package org.zaproxy.zap.extension.policyverifier.controllers;

import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.policyverifier.models.Policy;
import org.zaproxy.zap.extension.policyverifier.models.RuleEnforcingPassiveScanner;

import java.io.File;
import java.util.Objects;

/**
 * This class manages manages communication between the view and the model
 * It is a singleton, because only one instance is used, by one entity only.
 */
public class PolicyLoaderController {
    private static PolicyLoaderController soleController;

    public PolicyLoaderController() {
        if (soleController != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static PolicyLoaderController getSingleton() {
        if (soleController == null) {
            soleController = new PolicyLoaderController();
        }
        return soleController;
    }

    /**
     * When the user import a policy, the view will notify the controller and send the inputted file to it.
     * This method delegates the generation of a policy and add it to the model, which is a PluginPassiveScanner
     *
     * @param jar File representing the loaded policy
     */
    public void loadPolicy(File jar) {
        Policy loadedPolicy;
        try {
            loadedPolicy = PolicyGeneratorFromJar.generatePolicy(jar);
            RuleEnforcingPassiveScanner.getSingleton().addPolicy(loadedPolicy);
        } catch (Exception e) {
            Objects.requireNonNull(View.getSingleton()).showWarningDialog("Could not load and instantiate the policy classes.");
        }
    }
}
