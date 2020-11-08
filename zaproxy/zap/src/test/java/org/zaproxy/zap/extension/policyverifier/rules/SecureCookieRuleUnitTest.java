package org.zaproxy.zap.extension.policyverifier.rules;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class SecureCookieRuleUnitTest {
    SecureCookie rule;
    private List<String> flags;

    @BeforeEach
    public void setup() {
        rule = new SecureCookie();
        flags = new ArrayList<>(Arrays.asList("Secure", "HttpOnly", "SameSite"));
    }

    @Test
    public void isValid_CookiesContainAllTheFlags_Valid() {
        HttpMessage msg = new HttpMessage();
        TreeSet<HtmlParameter> cookieParams = new TreeSet<HtmlParameter>();
        for (String flag : flags) {
            cookieParams.add(new HtmlParameter(flag));
        }
        msg.setCookieParams(cookieParams);
        System.out.println(msg.getResponseHeader());
        assertTrue(rule.isValid(msg));
    }

    @Test
    public void isValid_CookiesContainOnlySomeOfValidFlags_NotValid() {
        HttpMessage msg = new HttpMessage();
        TreeSet<HtmlParameter> cookieParams = new TreeSet<HtmlParameter>();
        Random rand = new Random();
        flags.remove(rand.nextInt(flags.size()));
        for (String flag : flags) {
            cookieParams.add(new HtmlParameter(flag));
        }
        msg.setCookieParams(cookieParams);
        assertFalse(rule.isValid(msg));
    }

    @Test
    public void isValid_EmptyCookieParameters_NotValid() {
        HttpMessage msg = new HttpMessage();
        TreeSet<HtmlParameter> cookieParams = new TreeSet<HtmlParameter>();
        msg.setCookieParams(cookieParams);
        assertFalse(rule.isValid(msg));
    }

    @Test
    public void isValid_NoCookiesAtAll_Valid() {
        HttpMessage msg = new HttpMessage();
        assertTrue(rule.isValid(msg));
    }
}
