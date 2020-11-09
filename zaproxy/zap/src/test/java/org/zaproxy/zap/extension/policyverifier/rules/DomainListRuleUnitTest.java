package org.zaproxy.zap.extension.policyverifier.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/** Unit test for {@link DomainListRule}. */
public class DomainListRuleUnitTest {
    private DomainListRule rule;
    private HttpMessage msg;

    @BeforeEach
    public void setUp() throws Exception {
        rule = new DomainListRule(DomainListRule.DOMAINS);
        msg = mock(HttpMessage.class);
    }

    private HttpMessage getMockMessage(String hostname) {
        HttpMessage msg = mock(HttpMessage.class, RETURNS_DEEP_STUBS);
        when(msg.getRequestHeader().getHostName()).thenReturn(hostname);
        return msg;
    }

    @Test
    public void shouldNotFlagUnlistedDomain() throws HttpMalformedHeaderException {
        // Given
        HttpMessage msg = getMockMessage("example.com");
        // When
        boolean isValid = rule.isValid(msg);
        // Then
        assertThat(isValid, is(equalTo(true)));
    }

    @Test
    public void shouldFlagListedDomain() throws HttpMalformedHeaderException {
        // Given
        HttpMessage msg = getMockMessage(DomainListRule.DOMAINS[0]);
        // When
        boolean isValid = rule.isValid(msg);
        // Then
        assertThat(isValid, is(equalTo(false)));
    }

    @Test
    public void shouldFlagListedSubdomain() throws HttpMalformedHeaderException {
        // Given
        HttpMessage msg = getMockMessage("www." + DomainListRule.DOMAINS[0]);
        // When
        boolean isValid = rule.isValid(msg);
        // Then
        assertThat(isValid, is(equalTo(false)));
    }

    @Test
    public void shouldNotFlagContainingListed() throws HttpMalformedHeaderException {
        // Given
        HttpMessage msg = getMockMessage("not" + DomainListRule.DOMAINS[0]);
        // When
        boolean isValid = rule.isValid(msg);
        // Then
        assertThat(isValid, is(equalTo(true)));
    }
}
