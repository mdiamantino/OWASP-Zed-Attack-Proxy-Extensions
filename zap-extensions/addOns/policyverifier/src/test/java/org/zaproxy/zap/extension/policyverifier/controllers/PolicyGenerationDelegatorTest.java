package org.zaproxy.zap.extension.policyverifier.controllers;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.zaproxy.zap.extension.policyverifier.models.Policy;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PolicyGenerationDelegatorTest {

    private String TEST_RESOURCES_PATH =
            System.getProperty("user.dir")
                    + "/src/test/resources/org/zaproxy/zap/extension/policyverifier";

    @Test
    void policyDelegatorWithJar() throws Exception {
        File jarFile = new File(TEST_RESOURCES_PATH + "/PolicyExample.jar");
        PolicyGenerationDelegator delegator = new PolicyGenerationDelegator();
        Policy policy = delegator.generatePolicy(jarFile);
        assertEquals(FilenameUtils.removeExtension(jarFile.getName()), policy.getName());
    }

    @Test
    void policyDelegatorWithTxt() throws Exception {
        File jarFile = new File(TEST_RESOURCES_PATH + "/expression.txt");
        PolicyGenerationDelegator delegator = new PolicyGenerationDelegator();
        Policy policy = delegator.generatePolicy(jarFile);
        assertEquals(FilenameUtils.removeExtension(jarFile.getName()), policy.getName());
    }
}
