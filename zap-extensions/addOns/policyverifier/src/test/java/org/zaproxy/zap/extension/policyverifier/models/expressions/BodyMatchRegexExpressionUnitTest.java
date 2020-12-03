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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;

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
