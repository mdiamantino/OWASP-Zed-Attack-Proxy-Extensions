package org.zaproxy.zap.extension.policyverifier.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class EmailRuleUnitTest {
    private EmailRule rule;

    @BeforeEach
    public void setUp() throws Exception {
        rule = new EmailRule();
    }

    private HttpMessage getMockMessage(String headers, String body) {
        HttpMessage msg = mock(HttpMessage.class, RETURNS_DEEP_STUBS);
        when(msg.getRequestHeader().isText()).thenReturn(true);
        when(msg.getRequestHeader().getHeadersAsString()).thenReturn(headers);
        when(msg.getRequestBody().toString()).thenReturn(body);
        return msg;
    }

    @Test
    public void shouldNotFlagRequestWithoutEmail() throws HttpMalformedHeaderException {
        // Given
        HttpMessage msg = getMockMessage("", "username=example");
        // When
        boolean isValid = rule.isValid(msg);
        // Then
        assertThat(isValid, is(equalTo(true)));
    }

    @Test
    public void shouldFlagPostRequestWithEmail() throws HttpMalformedHeaderException {
        // Given
        HttpMessage msg = getMockMessage("", "email=example@example.com");
        // When
        boolean isValid = rule.isValid(msg);
        // Then
        assertThat(isValid, is(equalTo(false)));
    }

    @Test
    public void shouldFlagPostGetRequestWithEmail() throws HttpMalformedHeaderException {
        // Given
        HttpMessage msg = getMockMessage("GET /?email=example@example.com HTTP/1.1\n", "");
        // When
        boolean isValid = rule.isValid(msg);
        // Then
        assertThat(isValid, is(equalTo(false)));
    }
}
