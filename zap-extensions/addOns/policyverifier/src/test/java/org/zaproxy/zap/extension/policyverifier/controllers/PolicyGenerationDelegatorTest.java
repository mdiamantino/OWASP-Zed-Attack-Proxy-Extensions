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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zaproxy.zap.extension.policyverifier.models.Policy;

public class PolicyGenerationDelegatorTest {
    private PolicyGenerationDelegator delegator;

    private String TEST_RESOURCES_PATH =
            System.getProperty("user.dir")
                    + "/src/test/resources/org/zaproxy/zap/extension/policyverifier";

    @BeforeEach
    void setup() {
        delegator = new PolicyGenerationDelegator();
    }

    @Test
    void generatePolicy_CorrectJarFile_GenerationSuccess() throws Exception {
        File jarFile = new File(TEST_RESOURCES_PATH + "/PolicyExample.jar");
        Policy policy = delegator.generatePolicy(jarFile);
        assertEquals(FilenameUtils.removeExtension(jarFile.getName()), policy.getName());
    }

    @Test
    void generatePolicy_CorrectTxtFile_GenerationSuccess() throws Exception {
        File txtFile = new File(TEST_RESOURCES_PATH + "/txtFiles/GoodExpression.txt");
        Policy policy = delegator.generatePolicy(txtFile);
        assertEquals(FilenameUtils.removeExtension(txtFile.getName()), policy.getName());
    }

    @Test
    void generatePolicy_IncorrectFileExtension_ShouldThrows() {
        File jarFile = new File(TEST_RESOURCES_PATH + "/BadExpressionExtension.bad");
        assertThrows(AssertionError.class, () -> delegator.generatePolicy(jarFile));
    }
}
