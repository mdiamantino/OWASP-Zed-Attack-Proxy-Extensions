package org.zaproxy.zap.extension.policyverifier.models.expressions.regex;

import org.apache.commons.lang.IncompleteArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.Subject;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.MatchRegexTerminalExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class HeaderMatchRegexExpressionUnitTest {
    HttpMessage msg;
    MatchRegexTerminalExpression matchRegexTerminalExpression;

    @BeforeEach
    public void setUp() throws Exception {
        msg = mock(HttpMessage.class);
        matchRegexTerminalExpression = new MatchRegexTerminalExpression();
    }

    private HttpMessage getMockMessageRequest(String headerName, String headerValue) {
        HttpMessage msg = mock(HttpMessage.class, RETURNS_DEEP_STUBS);
        when(msg.getRequestHeader().getHeader(headerName)).thenReturn(headerValue);
        return msg;
    }

    private HttpMessage getMockMessageResponse(String headerName, String headerValue) {
        HttpMessage msg = mock(HttpMessage.class, RETURNS_DEEP_STUBS);
        when(msg.getResponseHeader().getHeader(headerName)).thenReturn(headerValue);
        return msg;
    }

    @Test
    public void testContent_Regex_ReqContainsRegex() {
        Subject subject = Subject.REQUEST_HEADER;
        String headerName = "accept";
        String pattern = "(?:.)*(plain)(?:.)*";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, pattern));
        matchRegexTerminalExpression.setSubjectAndValues(subject, values);

        String headerValue = "plain/text";
        HttpMessage msg = getMockMessageRequest(headerName, headerValue);
        assertTrue(matchRegexTerminalExpression.test(msg));
    }

    @Test
    public void testContent_Regex_NoExpectedReqHeader() {
        Subject subject = Subject.REQUEST_HEADER;
        String headerName = "accept";
        String pattern = "(?:.)*(plain)(?:.)*";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, pattern));
        matchRegexTerminalExpression.setSubjectAndValues(subject, values);

        HttpMessage msg = getMockMessageRequest(headerName, "");
        assertTrue(matchRegexTerminalExpression.test(msg));
    }

    @Test
    public void testContent_Regex_ReqNotContainsRegex() {
        Subject subject = Subject.REQUEST_HEADER;
        String headerName = "accept";
        String pattern = "(?:.)*(plain)(?:.)*";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, pattern));
        matchRegexTerminalExpression.setSubjectAndValues(subject, values);

        String headerValue = "application/json";
        HttpMessage msg = getMockMessageRequest(headerName, headerValue);
        assertFalse(matchRegexTerminalExpression.test(msg));
    }

    @Test
    public void testContent_Regex_ResContainsRegex() {
        Subject subject = Subject.RESPONSE_HEADER;
        String headerName = "content-type";
        String pattern = "(?:.)*(plain)(?:.)*";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, pattern));
        matchRegexTerminalExpression.setSubjectAndValues(subject, values);

        String headerValue = "plain/text";
        HttpMessage msg = getMockMessageResponse(headerName, headerValue);
        assertTrue(matchRegexTerminalExpression.test(msg));
    }

    @Test
    public void testContent_Regex_NoExpectedResHeader() {
        Subject subject = Subject.RESPONSE_HEADER;
        String headerName = "content-type";
        String pattern = "(?:.)*(plain)(?:.)*";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, pattern));
        matchRegexTerminalExpression.setSubjectAndValues(subject, values);

        HttpMessage msg = getMockMessageResponse(headerName, "");
        assertTrue(matchRegexTerminalExpression.test(msg));
    }

    @Test
    public void testContent_Regex_ResNotContainsRegex() {
        Subject subject = Subject.RESPONSE_HEADER;
        String headerName = "content-type";
        String pattern = "(?:.)*(plain)(?:.)*";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, pattern));
        matchRegexTerminalExpression.setSubjectAndValues(subject, values);

        String headerValue = "application/json";
        HttpMessage msg = getMockMessageResponse(headerName, headerValue);
        assertFalse(matchRegexTerminalExpression.test(msg));
    }
}
