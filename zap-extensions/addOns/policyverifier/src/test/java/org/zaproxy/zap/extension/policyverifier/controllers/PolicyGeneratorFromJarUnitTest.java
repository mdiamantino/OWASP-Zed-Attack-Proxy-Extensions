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

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zaproxy.zap.extension.policyverifier.controllers.jarLoader.PolicyGeneratorFromJar;
import org.zaproxy.zap.extension.policyverifier.models.Policy;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

public class PolicyGeneratorFromJarUnitTest {
    private PolicyGeneratorFromJar policyGeneratorFromJar;
    private String TEST_RESOURCES_PATH =
            System.getProperty("user.dir")
                    + "/src/test/java/org/zaproxy/zap/extension/policyverifier/controllers/resources";

    @BeforeEach
    public void setUp() throws Exception {
        policyGeneratorFromJar = new PolicyGeneratorFromJar();
    }

    /**
     * Testing a standard case in which the jar contains two classes representing rules
     *
     * @throws Exception if problem in generating policy
     */
    @Test
    public void generatePolicy_JarContainsCorrect() {
        File jarForTest = new File(TEST_RESOURCES_PATH + "/PolicyExample.jar");
        Set<String> rulesNames =
                new HashSet<>(
                        Arrays.asList("MissingContentTypeHeader", "CrossDomainScriptInclusion"));
        Policy policy = null;
        try {
            policyGeneratorFromJar.setFile(jarForTest);
            policy = policyGeneratorFromJar.generatePolicy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(policy.getName(), "PolicyExample");
        Set<Rule> extractedRules = policy.getRules();
        assertEquals(extractedRules.size(), 2);
        for (Rule rule : extractedRules) {
            assertTrue(rulesNames.contains(rule.getName()));
        }
    }

    /**
     * Testing the case in which file is empty
     *
     * @throws IOException if could not create the temp file
     */
    @Test
    public void generatePolicy_emptyFile_shouldThrowIllegalArgEx() throws IOException {
        File temp = File.createTempFile("testfile", ".jar");
        policyGeneratorFromJar.setFile(temp);
        assertThrows(IllegalArgumentException.class, () -> policyGeneratorFromJar.generatePolicy());
        temp.deleteOnExit();
    }

    /** Testing the case in which there are classes in the jar which do not inherit from Rule */
    @Test
    public void generatePolicy_nonRuleClassInJar_shouldThrowClassCastException() {
        File jarForTest = new File(TEST_RESOURCES_PATH + "/NonRuleClass.jar");
        policyGeneratorFromJar.setFile(jarForTest);
        assertThrows(ClassCastException.class, () -> policyGeneratorFromJar.generatePolicy());
    }

    /** Testing the case in which the jar contains only Java files which have not been compiled */
    @Test
    public void generatePolicy_OnlyJavaFilesInJar_shouldThrowIllegalArgException() {
        File jarForTest = new File(TEST_RESOURCES_PATH + "/PolicyWithJavaFilesOnly.jar");
        policyGeneratorFromJar.setFile(jarForTest);
        assertThrows(IllegalArgumentException.class, () -> policyGeneratorFromJar.generatePolicy());
    }

    /** Testing the case in which the jar contains a rule that cannot be instanciated */
    @Test
    public void generatePolicy_UninstanciableRuleInJar_shouldThrowIllegalArgException() {
        File jarForTest = new File(TEST_RESOURCES_PATH + "/PolicyWithJavaFilesOnly.jar");
        policyGeneratorFromJar.setFile(jarForTest);
        assertThrows(IllegalArgumentException.class, () -> policyGeneratorFromJar.generatePolicy());
    }
}
