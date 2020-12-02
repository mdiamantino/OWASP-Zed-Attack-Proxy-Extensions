package org.zaproxy.zap.extension.policyverifier.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zaproxy.zap.testutils.TestUtils;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class PolicyLoaderControllerTest extends TestUtils {
    private final String TEST_RESOURCES_PATH =
            System.getProperty("user.dir")
                    + "/src/test/resources/org/zaproxy/zap/extension/policyverifier/";

    @BeforeEach
    public void setUp() throws Exception {
        setUpZap();
    }

    @Test
    void loadPolicy() {
        String dirPath =System.getProperty("user.dir");
        File txtPolicy = new File(TEST_RESOURCES_PATH + "/GoodExpression.txt");
        PolicyLoaderController policyLoaderController = new PolicyLoaderController();
        policyLoaderController.loadPolicy(txtPolicy);

    }

}