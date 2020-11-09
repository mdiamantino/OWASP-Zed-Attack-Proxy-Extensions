package org.zaproxy.zap.extension.policyverifier.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class NoEmailsUnitTest {
    private NoEmails rule;

    @BeforeEach
    public void setUp() throws Exception {
        rule = new NoEmails();
    }

    private HttpMessage getMockMessage(String headers, String body) {
        HttpMessage msg = mock(HttpMessage.class, RETURNS_DEEP_STUBS);
        when(msg.getRequestHeader().isText()).thenReturn(true);
        when(msg.getRequestHeader().getHeadersAsString()).thenReturn(headers);
        when(msg.getRequestBody().toString()).thenReturn(body);
        return msg;
    }

    @Test
    public void shouldNotFlagRequestWithoutEmail() {
        HttpMessage msg = getMockMessage("", "username=example");
        assertTrue(rule.isValid(msg));
    }

    @Test
    public void shouldFlagPostRequestWithEmail() {
        HttpMessage msg = getMockMessage("", "email=example@example.com");
        assertFalse(rule.isValid(msg));
    }

    @Test
    public void shouldFlagPostGetRequestWithEmail() {
        HttpMessage msg = getMockMessage("GET /?email=example@example.com HTTP/1.1\n", "");
        assertFalse(rule.isValid(msg));
    }
}
