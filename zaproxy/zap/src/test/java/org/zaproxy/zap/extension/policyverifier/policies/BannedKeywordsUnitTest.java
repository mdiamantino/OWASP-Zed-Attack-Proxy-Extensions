package org.zaproxy.zap.extension.policyverifier.policies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;

public class BannedKeywordsUnitTest {

    BannedKeywordsRule rule = new BannedKeywordsRule();

    @Test
    public void shouldSucceedBannedKeywords() {
        // Given
        HttpMessage msg = new HttpMessage();
        msg.setRequestBody("Valid");

        // When
        boolean isValid = rule.isValid(msg, null);

        // Then
        assertThat(true, is(isValid));
    }

    @Test
    public void shouldFailBannedKeywordsUpperCase() {
        // Given
        HttpMessage msg = new HttpMessage();
        msg.setRequestBody("SELECT");

        // When
        boolean isValid = rule.isValid(msg, null);

        // Then
        assertThat(false, is(isValid));
    }


    @Test
    public void shouldFailBannedKeywordsLowerCase() {
        // Given
        HttpMessage msg = new HttpMessage();
        msg.setRequestBody("select");

        // When
        boolean isValid = rule.isValid(msg, null);

        // Then
        assertThat(false, is(isValid));
    }
}
