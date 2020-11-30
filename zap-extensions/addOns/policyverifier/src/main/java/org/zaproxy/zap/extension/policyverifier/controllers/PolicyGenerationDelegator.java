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

import org.apache.commons.io.FilenameUtils;
import org.zaproxy.zap.extension.policyverifier.controllers.jarLoader.PolicyGeneratorFromJar;
import org.zaproxy.zap.extension.policyverifier.controllers.txtLoader.PolicyGeneratorFromTxt;
import org.zaproxy.zap.extension.policyverifier.models.Policy;

import java.io.File;

public class PolicyGenerationDelegator {
    PolicyGenerator policyGenerator;

    public Policy generatePolicy(File file) throws Exception {
        String extension = FilenameUtils.getExtension(file.getName());
        if (extension.equals("jar")) {
            policyGenerator = new PolicyGeneratorFromJar();
        } else if (extension.equals("txt")) {
            policyGenerator = new PolicyGeneratorFromTxt();
        }
        assert policyGenerator != null;
        policyGenerator.setFile(file);
        return policyGenerator.generatePolicy();
    }
}
