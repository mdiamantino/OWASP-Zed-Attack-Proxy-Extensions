package org.zaproxy.zap.extension.policyverifier.controllers;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PolicyLoaderControllerUnitTest {
    private String TEST_RESOURCES_PATH = System.getProperty("user.dir") + "/src/test/java/org/zaproxy/zap/extension/policyverifier/controllers/resources";

    @Test
    public void PolicyLoaderController_InstantiatingTwice_ShouldThrowBecauseIsSingleton() {
        PolicyLoaderController possiblySingletonInstance = PolicyLoaderController.getSingleton();
        assertThrows(RuntimeException.class, PolicyLoaderController::new);
        assertEquals(PolicyLoaderController.getSingleton(), possiblySingletonInstance);
    }

    @Test
    public void loadPolicy_ProblematicJar_ShouldThrow(){
        PolicyLoaderController plc = PolicyLoaderController.getSingleton();
        File problematicJar = new File(TEST_RESOURCES_PATH+"/NonRuleClass.jar");
        assertThrows(StackOverflowError.class, () -> plc.loadPolicy(problematicJar));
    }
}
