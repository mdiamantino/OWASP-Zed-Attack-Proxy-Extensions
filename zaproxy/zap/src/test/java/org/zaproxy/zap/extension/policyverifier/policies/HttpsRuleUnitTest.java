package org.zaproxy.zap.extension.policyverifier.policies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.apache.commons.httpclient.HttpsURL;
import org.apache.commons.httpclient.URIException;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;


public class HttpsRuleUnitTest {

    HttpsRules rule = new HttpsRules();

    @Test
    public void shouldSucceedHttps() throws URIException {
        // Given
        HttpMessage httpMessage = new HttpMessage();
        httpMessage.getRequestHeader().setURI(new HttpsURL("https://www.google.com"));

        // When
        boolean isValid = rule.isValid(httpMessage);

        // Then
        assertThat(true, is(isValid));
    }


    @Test
    public void shouldFailHttps() throws URIException {
        // Given
        HttpMessage httpMessage = new HttpMessage();
        httpMessage.getRequestHeader().setURI(new HttpsURL("http://www.google.com"));

        // When
        boolean isValid = rule.isValid(httpMessage);

        // Then
        assertThat(false, is(isValid));
    }
}
