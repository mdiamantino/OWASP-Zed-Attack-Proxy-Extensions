package org.zaproxy.zap.extension.policyverifier.models.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.expressions.Subject;
import org.zaproxy.zap.extension.policyverifier.models.expressions.MatchRegexTerminalExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BodyMatchRegexExpressionUnitTest {
    HttpMessage msg;
    MatchRegexTerminalExpression matchRegexTerminalExpression;

    @BeforeEach
    public void setUp() throws Exception {
        msg = mock(HttpMessage.class);
        matchRegexTerminalExpression = new MatchRegexTerminalExpression();
    }

    private HttpMessage getMockMessageRequest(String bodyValue) {
        HttpMessage msg = mock(HttpMessage.class, RETURNS_DEEP_STUBS);
        when(msg.getRequestBody().toString()).thenReturn(bodyValue);
        return msg;
    }

    private HttpMessage getMockMessageResponse(String bodyValue) {
        HttpMessage msg = mock(HttpMessage.class, RETURNS_DEEP_STUBS);
        when(msg.getResponseBody().toString()).thenReturn(bodyValue);
        return msg;
    }

    @Test
    public void testContent_Regex_ReqContainsRegex() {
        Subject subject = Subject.REQUEST_BODY;
        String pattern = "(?:.)*(user)(?:.)*";
        List<String> values = new ArrayList<>(Arrays.asList(pattern));
        matchRegexTerminalExpression.setSubjectAndValues(subject, values);

        String bodyValue = "\"user\":\"user1\"";
        HttpMessage msg = getMockMessageRequest(bodyValue);
        assertTrue(matchRegexTerminalExpression.test(msg));
    }

    @Test
    public void testContent_Regex_ReqNotContainsRegex() {
        Subject subject = Subject.REQUEST_BODY;
        String pattern = "(?:.)*(user)(?:.)*";
        List<String> values = new ArrayList<>(Arrays.asList(pattern));
        matchRegexTerminalExpression.setSubjectAndValues(subject, values);

        String bodyValue = "\"hello\":\"hello\"";
        HttpMessage msg = getMockMessageRequest(bodyValue);
        assertFalse(matchRegexTerminalExpression.test(msg));
    }


    @Test
    public void testContent_Regex_NoReqBody() {
        Subject subject = Subject.REQUEST_BODY;
        String pattern = "(?:.)*(user)(?:.)*";
        List<String> values = new ArrayList<>(Arrays.asList(pattern));
        matchRegexTerminalExpression.setSubjectAndValues(subject, values);

        HttpMessage msg = getMockMessageRequest("");
        assertTrue(matchRegexTerminalExpression.test(msg));
    }

    @Test
    public void testContent_Regex_ResContainsRegex() {
        Subject subject = Subject.RESPONSE_BODY;
        String pattern = "(?:.)*(user)(?:.)*";
        List<String> values = new ArrayList<>(Arrays.asList(pattern));
        matchRegexTerminalExpression.setSubjectAndValues(subject, values);

        String bodyValue = "\"user\":\"user1\"";
        HttpMessage msg = getMockMessageResponse(bodyValue);
        assertTrue(matchRegexTerminalExpression.test(msg));
    }

    @Test
    public void testContent_Regex_NoResBody() {
        Subject subject = Subject.RESPONSE_BODY;
        String pattern = "(?:.)*(user)(?:.)*";
        List<String> values = new ArrayList<>(Arrays.asList(pattern));
        matchRegexTerminalExpression.setSubjectAndValues(subject, values);

        HttpMessage msg = getMockMessageResponse("");
        assertTrue(matchRegexTerminalExpression.test(msg));
    }

    @Test
    public void testContent_Regex_ResNotContainsRegex() {
        Subject subject = Subject.RESPONSE_BODY;
        String pattern = "(?:.)*(user)(?:.)*";
        List<String> values = new ArrayList<>(Arrays.asList(pattern));
        matchRegexTerminalExpression.setSubjectAndValues(subject, values);

        String bodyValue = "\"hello\":\"hello\"";
        HttpMessage msg = getMockMessageResponse(bodyValue);
        assertFalse(matchRegexTerminalExpression.test(msg));
    }
}
