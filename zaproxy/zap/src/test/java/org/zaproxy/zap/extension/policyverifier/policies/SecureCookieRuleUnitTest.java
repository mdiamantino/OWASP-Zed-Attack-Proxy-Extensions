package org.zaproxy.zap.extension.policyverifier.policies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;

import java.util.TreeSet;

public class SecureCookieRuleUnitTest {
    SecureCookieRule rule = new SecureCookieRule();

    @Test
    public void shouldSucceedCookieRule() {
        // Given
        HttpMessage msg = new HttpMessage();
        TreeSet<HtmlParameter> cookieParams = new TreeSet<HtmlParameter>();
        cookieParams.add(new HtmlParameter("Secure"));
        cookieParams.add(new HtmlParameter("HttpOnly"));
        cookieParams.add(new HtmlParameter("SameSite"));
        msg.setCookieParams(cookieParams);

        // When
        boolean isValid = rule.isValid(msg, null);

        // Then
        assertThat(true, is(isValid));
    }

    @Test
    public void shouldFailCookieRuleMissingSecure() {
        HttpMessage msg = new HttpMessage();
        TreeSet<HtmlParameter> cookieParams = new TreeSet<HtmlParameter>();
        cookieParams.add(new HtmlParameter("HttpOnly"));
        cookieParams.add(new HtmlParameter("SameSite"));
        msg.setCookieParams(cookieParams);

        // When
        boolean isValid = rule.isValid(msg, null);

        // Then
        assertThat(false, is(isValid));
    }

    @Test
    public void shouldFailCookieRuleMissingHttpOnly() {
        // Given
        HttpMessage msg = new HttpMessage();
        TreeSet<HtmlParameter> cookieParams = new TreeSet<HtmlParameter>();
        cookieParams.add(new HtmlParameter("Secure"));
        cookieParams.add(new HtmlParameter("SameSite"));
        msg.setCookieParams(cookieParams);

        // When
        boolean isValid = rule.isValid(msg, null);

        // Then
        assertThat(false, is(isValid));
    }

    @Test
    public void shouldFailCookieRuleMissingSameSite() {
        // Given
        HttpMessage msg = new HttpMessage();
        TreeSet<HtmlParameter> cookieParams = new TreeSet<HtmlParameter>();
        cookieParams.add(new HtmlParameter("Secure"));
        cookieParams.add(new HtmlParameter("HttpOnly"));
        msg.setCookieParams(cookieParams);

        // When
        boolean isValid = rule.isValid(msg, null);

        // Then
        assertThat(false, is(isValid));
    }
}
