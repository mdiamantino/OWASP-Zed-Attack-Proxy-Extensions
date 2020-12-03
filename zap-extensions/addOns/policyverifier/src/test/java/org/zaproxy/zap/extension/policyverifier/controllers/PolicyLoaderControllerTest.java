package org.zaproxy.zap.extension.policyverifier.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;
import org.zaproxy.zap.extension.policyverifier.models.PoliciesReporter;
import org.zaproxy.zap.extension.policyverifier.models.Policy;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

class PolicyLoaderControllerTest {
    private final String TEST_RESOURCES_PATH =
            System.getProperty("user.dir")
                    + "/src/test/resources/org/zaproxy/zap/extension/policyverifier/txtFiles";


    @Test
    void loadPolicy_CorrectPolicy_AddedToModel() throws NoSuchFieldException {
        PolicyLoaderController policyLoaderController = Mockito.spy(PolicyLoaderController.class);
        PoliciesReporter policiesReporter = new PoliciesReporter();
        FieldSetter.setField(policyLoaderController, policyLoaderController.getClass().getDeclaredField("policiesReporter"), policiesReporter);
        policiesReporter.setTestMode();
        List<Policy> policies = new ArrayList<>();
        FieldSetter.setField(policiesReporter, policiesReporter.getClass().getDeclaredField("policies"), policies);
        File goodPolicy = new File(TEST_RESOURCES_PATH + "/GoodExpression.txt");
        policyLoaderController.loadPolicy(goodPolicy);
        assertEquals(1, policies.size());
        assertEquals("GoodExpression", policies.get(0).getName());
    }


    @Test
    void loadPolicy_BadPolicy_ThrowsInternally() throws NoSuchFieldException {
        PolicyLoaderController policyLoaderController = Mockito.spy(PolicyLoaderController.class);
        PoliciesReporter policiesReporter = new PoliciesReporter();
        FieldSetter.setField(policyLoaderController, policyLoaderController.getClass().getDeclaredField("policiesReporter"), policiesReporter);
        policiesReporter.setTestMode();
        List<Policy> policies = new ArrayList<>();
        FieldSetter.setField(policiesReporter, policiesReporter.getClass().getDeclaredField("policies"), policies);
        File goodPolicy = new File(TEST_RESOURCES_PATH + "/emptyPolicy.txt");
        boolean hasThrown = false;
        try {
            policyLoaderController.loadPolicy(goodPolicy);
        } catch (IllegalArgumentException | StackOverflowError | NullPointerException e) {
            hasThrown = true;
        }
        assertTrue(hasThrown);
    }


    @Test
    void loadFile_StandardFile_loadPolicyCalled() throws NoSuchFieldException {
        PolicyLoaderController policyLoaderController = Mockito.spy(PolicyLoaderController.class);
        setTestMode(policyLoaderController);
        doReturn(JFileChooser.APPROVE_OPTION).when(policyLoaderController).extractRc(any(), any(), any());
        File goodPolicy = new File(TEST_RESOURCES_PATH + "/GoodExpression.txt");
        // Policy loader creates a mock file chooser which mocks the selection of file giving the one above
        JFileChooser jFileChooser = Mockito.mock(JFileChooser.class);
        doReturn(jFileChooser).when(policyLoaderController).createFileChooser();
        doReturn(goodPolicy).when(jFileChooser).getSelectedFile();
        policyLoaderController.loadFile("Desc", "txt");
        Mockito.verify(policyLoaderController, Mockito.times(1)).loadPolicy(goodPolicy);
    }

    @Test
    void loadFile_emptyFile_MessageShowedToUser() throws NoSuchFieldException {
        PolicyLoaderController policyLoaderController = Mockito.spy(PolicyLoaderController.class);
        setTestMode(policyLoaderController);
        JFileChooser jFileChooser = Mockito.mock(JFileChooser.class);
        doReturn(jFileChooser).when(policyLoaderController).createFileChooser();
        doReturn(JFileChooser.APPROVE_OPTION).when(policyLoaderController).extractRc(any(), any(), any());
        doThrow(new RuntimeException("Empty")).when(policyLoaderController).alertMessage(any());
        assertThrows(RuntimeException.class, () -> policyLoaderController.loadFile("Desc", "txt"));
    }


    private void setTestMode(PolicyLoaderController policyLoaderController) throws NoSuchFieldException {
        PoliciesReporter policiesReporter = new PoliciesReporter();
        FieldSetter.setField(policyLoaderController, policyLoaderController.getClass().getDeclaredField("policiesReporter"), policiesReporter);
        policiesReporter.setTestMode();
    }

}