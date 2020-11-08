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
        Set<Rule> rules = instantiateRules();

        HttpMessage msg = new HttpMessage();
        Set<String> expectedInvalidRulesNames = buildRandomBehaviourAndGetExpectedResults(rules, msg);

        policy = new Policy(rules, POLICY_NAME_FOR_TEST);
        Set<String> invalidRules = policy.getViolatedRulesNames(msg);
        assertEquals(expectedInvalidRulesNames, invalidRules);
    }

    /**
     * Instantiate a set of mock Rules
     * @return Set of mock rules
     */
    private Set<Rule> instantiateRules() {
        // Creating mock Rules
        CrossDomainScriptInclusion cdsiRule = mock(CrossDomainScriptInclusion.class);
        MissingContentTypeHeader mcthRule = mock(MissingContentTypeHeader.class);
        BannedKeywords bkRule = mock(BannedKeywords.class);
        HttpsOnly hoRule = mock(HttpsOnly.class);
        SecureCookie scRule = mock(SecureCookie.class);

        Set<Rule> rules = new HashSet<>();
        rules.add(cdsiRule);
        rules.add(mcthRule);
        rules.add(bkRule);
        rules.add(hoRule);
        rules.add(scRule);
        return rules;
    }

    /**
     * Select at random the rules which will be invalid
     * @param rules Complete set of rules
     * @param msg Som HttpMessage
     * @return Set of rules' names expected to have failed the validity test upon the message
     */
    private Set<String> buildRandomBehaviourAndGetExpectedResults(Set<Rule> rules, HttpMessage msg) {
        Set<String> expectedInvalidRulesNames = new HashSet<>();
        Random random = new Random();
        for (Rule rule: rules) {
            Boolean willHttpMessageBeValidForRule = random.nextBoolean();
            when(rule.isValid(msg)).thenReturn(willHttpMessageBeValidForRule);
            if (!willHttpMessageBeValidForRule) {
                expectedInvalidRulesNames.add(rule.getName());
            }
        }
        return expectedInvalidRulesNames;
    }

}