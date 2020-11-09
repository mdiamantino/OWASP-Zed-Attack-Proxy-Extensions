package org.zaproxy.zap.extension.policyverifier.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MissingContentTypeHeaderUnitTest {
    NoMissingContentTypeHeader rule;

    private HttpMessage generateHttpMessage() throws HttpMalformedHeaderException {
        HttpMessage message = new HttpMessage();
        String url = TestUtils.buildDomainUrl("domain-example");
        message.setRequestHeader("GET " + url + " HTTP/1.1");
        String responseBody = "<html></html>";
        message.setResponseBody(responseBody);
        return message;
    }

    @BeforeEach
    public void setup() {
        rule = new NoMissingContentTypeHeader();
    }

    @Test
    public void isValid_ContentTypeHeaderPresent_Valid() throws HttpMalformedHeaderException {
        HttpMessage msg = generateHttpMessage();
        msg.setResponseHeader(String.format("HTTP/1.1 200 OK\n" +
                "Content-Type: text/html; charset=utf-8\n" +
                "Server: Apache\n" +
                "Content-Length: %d\n", msg.getResponseBody().toString().length()));
        assertTrue(rule.isValid(msg));
    }

    @Test
    public void isValid_ContentTypeHeaderPresentAndEmpty_NotValid() throws HttpMalformedHeaderException {
        HttpMessage msg = generateHttpMessage();
        msg.setResponseHeader(String.format("HTTP/1.1 200 OK\n" +
                "Content-Type: \n" +
                "Server: Apache\n" +
                "Content-Length: %d\n", msg.getResponseBody().toString().length()));
        assertFalse(rule.isValid(msg));
    }

    @Test
    public void isValid_ContentTypeHeaderNotThere_NotValid() throws HttpMalformedHeaderException {
        HttpMessage msg = generateHttpMessage();
        msg.setResponseHeader(String.format("HTTP/1.1 200 OK\n" +
                "Server: Apache\n" +
                "Content-Length: %d\n", msg.getResponseBody().toString().length()));
        assertFalse(rule.isValid(msg));
    }

    @Test
    public void isValid_NoBody_Valid() throws HttpMalformedHeaderException {
        HttpMessage msg = generateHttpMessage();
        msg.setResponseBody("");
        msg.setResponseHeader(String.format("HTTP/1.1 200 OK\n" +
                "Content-Type: \n" +
                "Server: Apache\n" +
                "Content-Length: %d\n", msg.getResponseBody().toString().length()));
        assertTrue(rule.isValid(msg));
    }
}
