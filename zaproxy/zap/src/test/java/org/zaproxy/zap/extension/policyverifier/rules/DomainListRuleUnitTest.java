package org.zaproxy.zap.extension.policyverifier.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

/** Unit test for {@link DomainListRule}. */
public class DomainListRuleUnitTest {
    private DomainListRule rule;

    @BeforeEach
    public void setUp() throws Exception {
        rule = new DomainListRule(DomainListRule.DOMAINS);
    }

    @Test
    public void shouldNotFlagUnlistedDomain() throws HttpMalformedHeaderException {
        // Given
        HttpMessage msg = mock(HttpMessage.class);
        msg.setRequestHeader("GET https://www.example.com/test/ HTTP/1.1");
        msg.setRequestBody("");
        // When
        boolean isValid = rule.isValid(msg);
        // Then
        assertThat(isValid, is(equalTo(true)));
    }

    @Test
    public void shouldFlagListedDomain() throws HttpMalformedHeaderException {
        // Given
        HttpMessage msg = mock(HttpMessage.class);
        msg.setRequestHeader("GET https://www." + rule.DOMAINS[0] + "/test/ HTTP/1.1");
        msg.setRequestBody("");
        // When
        boolean isValid = rule.isValid(msg);
        // Then
        assertThat(isValid, is(equalTo(false)));
    }
}
