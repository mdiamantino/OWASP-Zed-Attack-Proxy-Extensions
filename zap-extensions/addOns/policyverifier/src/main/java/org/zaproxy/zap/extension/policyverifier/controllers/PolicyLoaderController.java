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
package org.zaproxy.zap.extension.policyverifier.controllers;

import java.io.File;
import java.util.Objects;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.policyverifier.models.Policy;
import org.zaproxy.zap.extension.policyverifier.models.RuleEnforcingPassiveScanner;

/**
 * This class manages manages communication between the view and the model It is a singleton,
 * because only one instance is used, by one entity only.
 */
public class PolicyLoaderController {
    private RuleEnforcingPassiveScanner reps;
    private String PREFIX = "policyverifier";
    private PolicyGeneratorFactory generatorDispatcher;

    public PolicyLoaderController() {
        reps = RuleEnforcingPassiveScanner.getSingleton();
        generatorDispatcher = new PolicyGeneratorFactory();
    }

    /**
     * When the user import a policy, the view will notify the controller and send the inputted file
     * to it. This method delegates the generation of a policy and add it to the model, which is a
     * PluginPassiveScanner
     *
     * @param file File representing the loaded policy
     */
    public void loadPolicy(File file) {
        Policy loadedPolicy;
        try {
            //            loadedPolicy = generatorDispatcher.generatePolicyFromFile(file);
            // Adding to model
            generatorDispatcher.setFile(file);
            loadedPolicy = generatorDispatcher.generatePolicy();
            reps.addPolicy(loadedPolicy);
        } catch (Exception e) {
            e.printStackTrace();
            Objects.requireNonNull(View.getSingleton())
                    .showWarningDialog(
                            Constant.messages.getString(PREFIX + ".loader.instantiationerror")
                                    + " :"
                                    + e);
        }
    }
}
