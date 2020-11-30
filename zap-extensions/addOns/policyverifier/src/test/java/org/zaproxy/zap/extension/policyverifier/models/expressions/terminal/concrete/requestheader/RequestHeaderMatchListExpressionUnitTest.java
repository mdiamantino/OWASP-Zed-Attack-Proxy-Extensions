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
package org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestheader;

import static org.junit.jupiter.api.Assertions.*;

public class RequestHeaderMatchListExpressionUnitTest {
    /*
    HttpMessage message;
    RequestHeaderMatchListExpression contentTypeExpression;

    @BeforeEach
    public void setUp() throws Exception {
        message = new HttpMessage();
        message.setRequestHeader("GET " + "http://insecure-domain-example.com" + " HTTP/1.1");

        List<String> args = Arrays.asList("Content-Type", " ");
        contentTypeExpression = new RequestHeaderMatchListExpression(args);
    }

    @Test
    public void RequestHeaderMatchListExpression_throwsOnTooFewArgs() {
        List<String> args = Arrays.asList("Content-Type");
        assertThrows(
                IncompleteArgumentException.class,
                () -> new RequestHeaderMatchListExpression(args));
    }

    @Test
    public void getRelevantValue_exists() throws HttpMalformedHeaderException {
        String contentType = "text/html; charset=UTF-8";
        message.getRequestHeader().setHeader(HttpHeader.CONTENT_TYPE, contentType);

        contentTypeExpression.getRelevantValue(message);
        assertEquals(contentTypeExpression.getRelevantValue(message), contentType);
    }

    @Test
    public void getRelevantValue_notExists() throws HttpMalformedHeaderException {
        contentTypeExpression.getRelevantValue(message);
        assertNull(contentTypeExpression.getRelevantValue(message));
    }

    */
}
