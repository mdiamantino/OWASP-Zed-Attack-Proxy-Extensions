package org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestheader;

import org.apache.commons.lang.IncompleteArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.rules.NoBannedDomains;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class RequestHeaderMatchListExpressionUnitTest {
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
        assertThrows(IncompleteArgumentException.class, () -> new RequestHeaderMatchListExpression(args));
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
}
