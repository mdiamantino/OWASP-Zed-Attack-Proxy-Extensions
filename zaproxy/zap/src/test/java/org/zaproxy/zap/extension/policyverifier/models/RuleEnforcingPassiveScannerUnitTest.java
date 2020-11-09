package org.zaproxy.zap.extension.policyverifier.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;

import static org.mockito.Mockito.*;

import java.util.*;

public class RuleEnforcingPassiveScannerUnitTest {
    protected RuleEnforcingPassiveScanner rule;

    @BeforeEach
    public void setUp() throws Exception {
        rule = spy(new RuleEnforcingPassiveScanner());
    }

    @Test
    public void shouldDisplayAlertsWhenRulesAreViolated() {
        // Given
        Set<String> rules = new HashSet<>(Arrays.asList("a", "b"));
        Policy policy = mock(Policy.class);
        when(policy.getViolatedRulesNames(isA(HttpMessage.class))).thenReturn(rules);
        when(policy.getName()).thenReturn("policy");

        rule.addPolicy(policy);
        doNothing().when(rule).generateViolatedRuleReport(anyString());

        // When
        rule.scanHttpRequestSend(new HttpMessage(), 1);
//        try (MockedConstruction<RuleEnforcingPassiveScanner.AlertBuilder> mocked = mockConstruction(RuleEnforcingPassiveScanner.AlertBuilder.class)) {}

        // Then
        verify(rule, times(1)).generateViolatedRuleReport(contains("Policypolicy.Rulea"));
        verify(rule, times(1)).generateViolatedRuleReport(matches(" *Policypolicy\\.Ruleb.*"));
    }
}
