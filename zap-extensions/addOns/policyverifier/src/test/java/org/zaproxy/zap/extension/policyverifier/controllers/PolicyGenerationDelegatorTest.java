package org.zaproxy.zap.extension.policyverifier.controllers;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zaproxy.zap.extension.policyverifier.models.Policy;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertThrows(
                AssertionError.class,
                () -> delegator.generatePolicy(jarFile));
    }

}
