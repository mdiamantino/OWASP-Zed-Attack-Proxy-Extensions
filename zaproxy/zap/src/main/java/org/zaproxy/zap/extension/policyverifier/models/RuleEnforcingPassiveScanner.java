package org.zaproxy.zap.extension.policyverifier.models;

import net.htmlparser.jericho.Source;
import org.apache.log4j.Logger;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.pscan.ExtensionPassiveScan;
import org.zaproxy.zap.extension.pscan.PassiveScanThread;
import org.zaproxy.zap.extension.pscan.PluginPassiveScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The model is a container of Policies defined by a set of policies, however the model is also a kind of rule which
 * checks if sub-rules are valid or not.
 * If a rule is violated an alert is displayed
 */
public class RuleEnforcingPassiveScanner extends PluginPassiveScanner {
    private final List<Policy> policies = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(RuleEnforcingPassiveScanner.class);
    private final String NAME = "JARPolicyVerifier";
    private static RuleEnforcingPassiveScanner soleModel; // Model is unique
    private List<Alert> calledAlerts = new ArrayList<>();

    public static RuleEnforcingPassiveScanner getSingleton() {
        if (soleModel == null) {
            soleModel = new RuleEnforcingPassiveScanner();
        }
        return soleModel;
    }

    @Override
    public int getPluginId() {
        return 4830;
    }

    public RuleEnforcingPassiveScanner() {
        logger.warn("Starting Rule Enforcing Passive scanner");
        if (soleModel != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        // When the model is first created, then it is automatically enabled to receive https messages
//        this.setEnabled(true); // Does not seem to be needed here. Breaks the unit tests
    }

    /**
     * Method used to add a policy to the model
     *
     * @param policy Policy to add
     */
    public void addPolicy(Policy policy) {
        policies.add(policy);
        logger.info(String.format("Policy %s added", policy.getName()));
    }

    /**
     * Checks for the validity of each policy and delegates the creation of alerts when a Rule is violated
     *
     * @param msg HttpMessage that is checked against policies for validity.
     */
    private void displayViolatedPolicies(HttpMessage msg) {
        logger.warn("Validating HttpMessage against loaded policies.");
        for (Policy policy : policies) {
            Set<String> violatedRulesNames = policy.getViolatedRulesNames(msg);
            generatePolicyReport(policy.getName(), violatedRulesNames);
        }
    }

    /**
     * Violated rules raise an alert displayed on the UI in the "Alerts" panel with the following description format:
     * "PolicyX.RuleY violated"
     *
     * @param policyName         Name of the violated Policy
     * @param violatedRulesNames Set of the violated Policy Rules
     */
    private void generatePolicyReport(String policyName, Set<String> violatedRulesNames) {
        for (String violatedRule : violatedRulesNames) {
            String description = String.format("Policy%s.Rule%s violated", policyName, violatedRule);
            generateViolatedRuleReport(description);
        }
    }

    /**
     * Generates a graphical report (in the form of a ZAP alert), that a rule was violated
     *
     * @param description         The text to be displayed in the rule violation report
     */
    protected void generateViolatedRuleReport(String description) {
        AlertBuilder alertBuilder = newAlert().setDescription(description);
        calledAlerts.add(alertBuilder.build());
        alertBuilder.raise();
    }

    /**
     * @param msg Http request message to be checked for validity against rules.
     * @param id  - Not used -
     */
    @Override
    public void scanHttpRequestSend(HttpMessage msg, int id) {
        displayViolatedPolicies(msg);
    }

    /**
     * @param msg    Http response message to be checked for validity against rules.
     * @param id     - Not used -
     * @param source - Not used -
     */
    @Override
    public void scanHttpResponseReceive(HttpMessage msg, int id, Source source) {
        displayViolatedPolicies(msg);
    }

    @Override
    public void setParent(PassiveScanThread parent) {
        // Nothing to do
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * When the plugin is enabled it adds itself to the plugins of passive scanners, so that it can read HttpMessages
     * and carry out the above defined behaviours.
     *
     * @param enabled Boolean
     */
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            Control.getSingleton()
                    .getExtensionLoader()
                    .getExtension(ExtensionPassiveScan.class).addPassiveScanner(this);
        } else {
            Control.getSingleton()
                    .getExtensionLoader()
                    .getExtension(ExtensionPassiveScan.class).removePassiveScanner(this);
        }
    }

    // For testing purposes only
    public List<Alert> getCalledAlerts() {
        return calledAlerts;
    }


}
