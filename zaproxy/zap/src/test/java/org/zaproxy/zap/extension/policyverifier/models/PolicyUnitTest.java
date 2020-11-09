package org.zaproxy.zap.extension.policyverifier.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.rules.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PolicyUnitTest {
    private String POLICY_NAME_FOR_TEST = "PolicyExample";
    private Policy policy;

    @AfterEach
    public void setUp() throws Exception {
        policy = null;
    }

    @Test
    void getViolatedRulesNames() {
        Set<Rule> rules = TestUtils.instantiateRules();

        HttpMessage msg = new HttpMessage();
        Set<String> expectedInvalidRulesNames = TestUtils.buildRandomBehaviourAndGetExpectedResults(rules, msg);

        policy = new Policy(rules, POLICY_NAME_FOR_TEST);
        Set<String> invalidRules = policy.getViolatedRulesNames(msg);
        assertEquals(expectedInvalidRulesNames, invalidRules);
    }



}