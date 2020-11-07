package org.zaproxy.zap.extension.policyverifier.controllers;

import org.junit.jupiter.api.Test;
import org.zaproxy.zap.extension.policyverifier.models.Policy;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class PolicyGeneratorFromJarUnitTest {
    private String TEST_RESOURCES_PATH = System.getProperty("user.dir") + "/src/test/java/org/zaproxy/zap/extension/policyverifier/controllers/resources";

    /**
     * Testing a standard case in which the jar contains two classes representing rules
     *
     * @throws Exception if problem in generating policy
     */
    @Test
    public void generatePolicy_JarContainsCorrect() {
        File jarForTest = new File(TEST_RESOURCES_PATH + "/PolicyExample.jar");
        Set<String> rulesNames = new HashSet<>(Arrays.asList("MissingContentTypeHeader", "CrossDomainScriptInclusion"));
        Policy policy = null;
        try {
            policy = PolicyGeneratorFromJar.generatePolicy(jarForTest);
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
        assertThrows(IllegalArgumentException.class, () -> PolicyGeneratorFromJar.generatePolicy(temp));
        temp.deleteOnExit();
    }

    /**
     * Testing the case in which there are classes in the jar which do not inherit from Rule
     */
    @Test
    public void generatePolicy_nonRuleClassInJar_shouldThrowClassCastException() {
        File jarForTest = new File(TEST_RESOURCES_PATH + "/NonRuleClass.jar");
        assertThrows(ClassCastException.class, () -> PolicyGeneratorFromJar.generatePolicy(jarForTest));
    }

    /**
     * Testing the case in which the jar contains only Java files which have not been compiled
     */
    @Test
    public void generatePolicy_OnlyJavaFilesInJar_shouldThrowIllegalArgException() {
        File jarForTest = new File(TEST_RESOURCES_PATH + "/PolicyWithJavaFilesOnly.jar");
        assertThrows(IllegalArgumentException.class, () -> PolicyGeneratorFromJar.generatePolicy(jarForTest));
    }

    /**
     * Testing the case in which the jar contains a rule that cannot be instanciated
     */
    @Test
    public void generatePolicy_UninstanciableRuleInJar_shouldThrowIllegalArgException() {
        File jarForTest = new File(TEST_RESOURCES_PATH + "/PolicyWithJavaFilesOnly.jar");
        assertThrows(IllegalArgumentException.class, () -> PolicyGeneratorFromJar.generatePolicy(jarForTest));
    }
}
