package org.zaproxy.zap.extension.policyverifier.models;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class PoliciesReporterUnitTest {

    @Test
    public void generateReportWithAPolicy() {
        PoliciesReporter reporter = new PoliciesReporter();
        reporter.setTestMode();
        Policy policy = mock(Policy.class);
        HttpMessage msg = new HttpMessage();
        Set<String> violatedRulesNames = new HashSet<>();
        String ruleName = RandomStringUtils.randomAlphabetic(3);
        String policyName = RandomStringUtils.randomAlphabetic(3);
        String policiesDescription = "Policy%s.Rule%s violated;";
        violatedRulesNames.add(ruleName);
        given(policy.getViolatedRulesNames(msg)).willReturn(violatedRulesNames);
        given(policy.getName()).willReturn(policyName);
        reporter.addPolicy(policy);
        reporter.addPolicy(policy);
        assertEquals(String.format(policiesDescription, policyName, ruleName), reporter.generateReportOnAllPolicies(msg).trim());
    }

    @Test
    public void generateReportWithoutAPolicy() {
        PoliciesReporter reporter = new PoliciesReporter();
        assertEquals("", reporter.generateReportOnAllPolicies(new HttpMessage()).trim());
    }
}