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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.IncompleteArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;

public class ExpressionsParamsUnitTest {
    HttpMessage msg;
    MatchListTerminalExpression matchListTerminalExpression;
    MatchRegexTerminalExpression matchRegexTerminalExpression;

    @BeforeEach
    public void setUp() throws Exception {
        msg = mock(HttpMessage.class);
        matchListTerminalExpression = new MatchListTerminalExpression();
        matchRegexTerminalExpression = new MatchRegexTerminalExpression();
    }

    private HttpMessage getMockMessageRequestHeader(String headerName, String headerValue) {
        HttpMessage msg = mock(HttpMessage.class, RETURNS_DEEP_STUBS);
        when(msg.getRequestHeader().getHeader(headerName)).thenReturn(headerValue);
        return msg;
    }

    private HttpMessage getMockMessageRequestBody(String bodyValue) {
        HttpMessage msg = mock(HttpMessage.class, RETURNS_DEEP_STUBS);
        when(msg.getRequestBody().toString()).thenReturn(bodyValue);
        return msg;
    }

    @Test
    public void setSubjectsAndValues_Regex_IncompleteArgumentsBody() {
        Subject subject = Subject.REQUEST_BODY;
        List<String> values = new ArrayList<>();
        assertThrows(
                IncompleteArgumentException.class,
                () -> matchRegexTerminalExpression.setSubjectAndValues(subject, values));
    }

    @Test
    public void setSubjectsAndValues_Regex_TooManyArgumentsBody() {
        Subject subject = Subject.REQUEST_BODY;
        List<String> values = new ArrayList<>(Arrays.asList("(?:.)*(user)(?:.)*", ".html."));
        assertThrows(
                IncompleteArgumentException.class,
                () -> matchRegexTerminalExpression.setSubjectAndValues(subject, values));
    }

    @Test
    public void setSubjectAndValues_Regex_GetRelevantValue() {
        Subject subject = Subject.REQUEST_BODY;
        String pattern = "(?:.)*(user)(?:.)*";
        List<String> values = new ArrayList<>(Arrays.asList(pattern));
        matchRegexTerminalExpression.setSubjectAndValues(subject, values);

        String bodyValue = "\"user\":\"user1\"";
        HttpMessage msg = getMockMessageRequestBody(bodyValue);
        assertEquals(matchRegexTerminalExpression.getRelevantValue(msg), bodyValue);
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
        HttpMessage msg = getMockMessageRequestHeader(headerName, headerValue);

        assertEquals(matchListTerminalExpression.getRelevantValue(msg), headerValue);
    }

    @Test
    public void setSubjectsAndValues_Regex_IncompleteArguments() {
        Subject subject = Subject.REQUEST_HEADER;
        List<String> values = new ArrayList<>();
        assertThrows(
                IncompleteArgumentException.class,
                () -> matchRegexTerminalExpression.setSubjectAndValues(subject, values));
    }

    @Test
    public void setSubjectsAndValues_Regex_TooManyArgumentsHeader() {
        Subject subject = Subject.REQUEST_HEADER;
        String headerName = "content-type";
        String pattern1 = "(?:.)*(plain)(?:.)*";
        String pattern2 = "(?:.)*(html)(?:.)*";
        List<String> values = new ArrayList<>(Arrays.asList(headerName, pattern1, pattern2));
        assertThrows(
                IncompleteArgumentException.class,
                () -> matchRegexTerminalExpression.setSubjectAndValues(subject, values));
    }
}
