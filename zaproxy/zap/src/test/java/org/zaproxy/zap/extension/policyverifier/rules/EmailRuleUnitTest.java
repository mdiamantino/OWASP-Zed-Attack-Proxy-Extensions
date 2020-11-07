package org.zaproxy.zap.extension.policyverifier.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class EmailRuleUnitTest {
    private EmailRule rule;

    @BeforeEach
    public void setUp() throws Exception {
        rule = new EmailRule();
    }

    @Test
    public void shouldNotFlagRequestWithoutEmail() throws HttpMalformedHeaderException {
        // Given
        HttpMessage msg = mock(HttpMessage.class);
        msg.setRequestHeader("POST https://www.example.com/test/ HTTP/1.1");
        msg.setRequestBody("username=example");
        // When
        boolean isValid = rule.isValid(msg);
        // Then
        assertThat(isValid, is(equalTo(true)));
    }

    @Test
    public void shouldFlagRequestWithEmail() throws HttpMalformedHeaderException {
        // Given
        HttpMessage msg = mock(HttpMessage.class);
        msg.setRequestHeader("POST https://www.example.com/test/ HTTP/1.1");
        msg.setRequestBody("email=example@example.com");
        // When
        boolean isValid = rule.isValid(msg);
        // Then
        assertThat(isValid, is(equalTo(false)));
    }
}
