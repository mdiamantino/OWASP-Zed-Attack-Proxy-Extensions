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

import net.htmlparser.jericho.Source;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.pscan.ExtensionPassiveScan;
import org.zaproxy.zap.extension.pscan.PassiveScanThread;
import org.zaproxy.zap.extension.pscan.PluginPassiveScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * The model is a container of Policies defined by a set of policies, however the model is also a
 * kind of rule which checks if sub-rules are valid or not. If a rule is violated an alert is
 * displayed
 */
public class PolicyVerifierPassiveScanner extends PluginPassiveScanner {
    private final String NAME = "PolicyVerifier";
    private List<Alert> calledAlerts = new ArrayList<>();
    private static int PLUGIN_ID = 5000019;
    private PoliciesReporter policiesReporter;

    public static PolicyVerifierPassiveScanner getSingleton() {
        return (PolicyVerifierPassiveScanner)
                Control.getSingleton()
                        .getExtensionLoader()
                        .getExtension(ExtensionPassiveScan.class)
                        .getPluginPassiveScanner(PLUGIN_ID);
    }

    @Override
    public int getPluginId() {
        return PLUGIN_ID;
    }

    public void setPoliciesReporter(PoliciesReporter policiesReporter) {
        this.policiesReporter = policiesReporter;
    }

    /**
     * Generates a graphical report (in the form of a ZAP alert), that a rule was violated
     */
    protected void generateViolatedRuleReport(HttpMessage msg) {
        String policiesDescription = policiesReporter.generateReportOnAllPolicies(msg);
        if (policiesDescription.isEmpty()) return;
        AlertBuilder alertBuilder = newAlert().setDescription(policiesDescription);
        calledAlerts.add(alertBuilder.build());
        alertBuilder.raise();
    }

    /**
     * @param msg Http request message to be checked for validity against rules.
     * @param id  - Not used -
     */
    @Override
    public void scanHttpRequestSend(HttpMessage msg, int id) {
        generateViolatedRuleReport(msg);
    }

    /**
     * @param msg    Http response message to be checked for validity against rules.
     * @param id     - Not used -
     * @param source - Not used -
     */
    @Override
    public void scanHttpResponseReceive(HttpMessage msg, int id, Source source) {
        generateViolatedRuleReport(msg);
    }

    @Override
    public void setParent(PassiveScanThread parent) {
        // Nothing to do
    }

    @Override
    public String getName() {
        return NAME;
    }
}
