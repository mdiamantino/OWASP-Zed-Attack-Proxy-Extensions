/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2020 The ZAP Development Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zaproxy.zap.extension.policyverifier.models.expressions;


import org.apache.commons.lang.IncompleteArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.expressions.Subject;
import org.zaproxy.zap.extension.policyverifier.models.expressions.MatchListTerminalExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HeaderMatchListExpressionUnitTest {
    HttpMessage msg;
    MatchListTerminalExpression matchListTerminalExpression;

    @BeforeEach
    public void setUp() throws Exception {
        msg = mock(HttpMessage.class);
        matchListTerminalExpression = new MatchListTerminalExpression();
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
    public void setSubjectsAndValues_MatchList_IncompleteArguments() {
        Subject subject = Subject.REQUEST_HEADER;
        List<String> values = new ArrayList<>();
        assertThrows(
                IncompleteArgumentException.class,
                () -> matchListTerminalExpression.setSubjectAndValues(subject, values));
    }

    @Test
    public void setSubjectAndValues_MatchList_RequestHeaderCorrectRelevantValue() {
        Subject subject = Subject.REQUEST_HEADER;
        String headerName = "host";
        String headerValue = "jekyll.com";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, headerValue));

        matchListTerminalExpression.setSubjectAndValues(subject, values);
        HttpMessage msg = getMockMessageRequest(headerName, headerValue);

        assertEquals(matchListTerminalExpression.getRelevantValue(msg), headerValue);
    }

    @Test
    public void testContents_MatchList_RequestHeaderContainsSingleValue() {
        Subject subject = Subject.REQUEST_HEADER;
        String headerName = "host";
        String headerValue = "jekyll.com";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, headerValue));

        matchListTerminalExpression.setSubjectAndValues(subject, values);
        HttpMessage msg = getMockMessageRequest(headerName, headerValue);

        assertTrue(matchListTerminalExpression.test(msg));
    }

    @Test
    public void testContents_MatchList_RequestHeaderNotContainsSingleValue() {
        Subject subject = Subject.REQUEST_HEADER;
        String headerName = "host";
        String expectedValue = "jekyll.com";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, expectedValue));

        matchListTerminalExpression.setSubjectAndValues(subject, values);
        String headerValue = "google.com";
        HttpMessage msg = getMockMessageRequest(headerName, headerValue);

        assertFalse(matchListTerminalExpression.test(msg));
    }

    @Test
    public void testContents_MatchList_RequestHeaderContainsMultipleValues() {
        Subject subject = Subject.REQUEST_HEADER;
        String headerName = "accept";
        String expectedValue1 = "json";
        String expectedValue2 = "html";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, expectedValue1, expectedValue1));

        matchListTerminalExpression.setSubjectAndValues(subject, values);

        String headerValue = String.join(",", expectedValue1, expectedValue2);
        HttpMessage msg = getMockMessageRequest(headerName, headerValue);
        assertTrue(matchListTerminalExpression.test(msg));
    }

    @Test
    public void testContents_MatchList_RequestHeaderDoesNotExist() {
        Subject subject = Subject.REQUEST_HEADER;
        String headerName = "content-type";
        String expectedValue = "jekyll.com";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, expectedValue));

        matchListTerminalExpression.setSubjectAndValues(subject, values);
        HttpMessage msg = getMockMessageRequest(headerName, "");

        assertTrue(matchListTerminalExpression.test(msg));
    }

    @Test
    public void setSubjectAndValues_MatchList_ResponseHeaderCorrectRelevantValue() {
        Subject subject = Subject.RESPONSE_HEADER;
        String headerName = "content-type";
        String headerValue = "json";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, headerValue));

        matchListTerminalExpression.setSubjectAndValues(subject, values);
        HttpMessage msg = getMockMessageResponse(headerName, headerValue);

        assertEquals(matchListTerminalExpression.getRelevantValue(msg), headerValue);
    }

    @Test
    public void testContents_MatchList_ResponseHeaderContainsSingleValue() {
        Subject subject = Subject.RESPONSE_HEADER;
        String headerName = "content-type";
        String headerValue = "json";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, headerValue));

        matchListTerminalExpression.setSubjectAndValues(subject, values);
        HttpMessage msg = getMockMessageResponse(headerName, headerValue);

        assertTrue(matchListTerminalExpression.test(msg));
    }

    @Test
    public void testContents_MatchList_ResponseHeaderNotContainsSingleValue() {
        Subject subject = Subject.RESPONSE_HEADER;
        String headerName = "accept";
        String expectedValue = "json";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, expectedValue));

        matchListTerminalExpression.setSubjectAndValues(subject, values);
        String headerValue = "html";
        HttpMessage msg = getMockMessageResponse(headerName, headerValue);

        assertFalse(matchListTerminalExpression.test(msg));
    }

    @Test
    public void testContents_MatchList_ResponseHeaderContainsMultipleValues() {
        Subject subject = Subject.RESPONSE_HEADER;
        String headerName = "accept";
        String expectedValue1 = "json";
        String expectedValue2 = "html";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, expectedValue1, expectedValue1));

        matchListTerminalExpression.setSubjectAndValues(subject, values);

        String headerValue = String.join(",", expectedValue1, expectedValue2);
        HttpMessage msg = getMockMessageResponse(headerName, headerValue);
        assertTrue(matchListTerminalExpression.test(msg));
    }

    @Test
    public void testContents_MatchList_ResponseHeaderDoesNotExist() {
        Subject subject = Subject.REQUEST_HEADER;
        String headerName = "accept";
        String expectedValue = "json";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, expectedValue));

        matchListTerminalExpression.setSubjectAndValues(subject, values);
        HttpMessage msg = getMockMessageResponse(headerName, "");

        assertTrue(matchListTerminalExpression.test(msg));
    }
}
