package org.zaproxy.zap.extension.policyverifier.views;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.zaproxy.zap.extension.policyverifier.models.Policy;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class PolicyVerifierPanelTest {

    @Test
    void getSingleton_StandardCall_InstanceNotNull() {
        PolicyVerifierPanel policyVerifierPanel = PolicyVerifierPanel.getSingleton();
        assertNotNull(policyVerifierPanel);
    }

    @Test
    void getStringBuilderForPolicies_WithPolicies_ContainsPoliciesAndRulesNames() {
        PolicyVerifierPanel policyVerifierPanel = Mockito.mock(PolicyVerifierPanel.class);
        Policy policy = generatePolicy();
        List<Policy> listOfPolicies = new ArrayList<>();
        listOfPolicies.add(policy);
        when(policyVerifierPanel.getStringBuilderForPolicies(any())).thenCallRealMethod();
        String htmlCode = policyVerifierPanel.getStringBuilderForPolicies(listOfPolicies).toString();
        assertTrue(htmlCode.contains("PolicyName"));
        assertTrue(htmlCode.contains("rule1"));
        assertTrue(htmlCode.contains("rule2"));
    }

    private Policy generatePolicy() {
        Rule rule1 = Mockito.mock(Rule.class);
        doReturn("rule1").when(rule1).getName();
        Rule rule2 = Mockito.mock(Rule.class);
        doReturn("rule2").when(rule2).getName();
        Set<Rule> rules = new HashSet<>();
        rules.add(rule1);
        rules.add(rule2);
        return new Policy(rules, "PolicyName");
    }

    @Test
    void getJScrollPane_NormalCall_HasFixedScrollBarPolicy() {
        PolicyVerifierPanel policyVerifierPanel = PolicyVerifierPanel.getSingleton();
        JScrollPane scrollPane = policyVerifierPanel.getJScrollPane();
        assertEquals(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, scrollPane.getHorizontalScrollBarPolicy());

    }
}