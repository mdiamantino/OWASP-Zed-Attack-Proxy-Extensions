package org.zaproxy.zap.extension.policyverifier.rules;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
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
    public void isValid_CookiesContainAllTheFlags_Valid() throws HttpMalformedHeaderException {
        HttpMessage msg = constructHttpMessage(flags);
        System.out.println(msg.getResponseHeader());
        assertTrue(rule.isValid(msg));
    }

    @Test
    public void isValid_CookiesContainOnlySomeOfValidFlags_NotValid() throws HttpMalformedHeaderException {
        List<String> cookieParams = new ArrayList<>();
        Random rand = new Random();
        flags.remove(rand.nextInt(flags.size()));
        for (String flag : flags) {
            cookieParams.add(flag);
        }
        HttpMessage msg = constructHttpMessage(cookieParams);
        assertFalse(rule.isValid(msg));
    }

    @Test
    public void isValid_EmptyCookieParameters_NotValid() throws HttpMalformedHeaderException {
        HttpMessage msg = constructHttpMessage(new ArrayList<>());
        assertFalse(rule.isValid(msg));
    }

    @Test
    public void isValid_NoCookiesAtAll_Valid() {
        HttpMessage msg = new HttpMessage();
        assertTrue(rule.isValid(msg));
    }

    private HttpMessage constructHttpMessage(List<String> cookieParamsToInclude) throws HttpMalformedHeaderException {
        HttpMessage msg = new HttpMessage();
        msg.setRequestHeader("GET https://www.example.com/test/ HTTP/1.1");

        String cookieParams = String.join(";", cookieParamsToInclude);
        msg.setResponseBody("<html></html>");
        msg.setResponseHeader(
                "HTTP/1.1 200 OK\r\n"
                        + "Server: Apache-Coyote/1.1\r\n"
                        + "Set-Cookie: test=123; Path=/; " + cookieParams + "\r\n"
                        + "Content-Type: text/html;charset=ISO-8859-1\r\n"
                        + "Content-Length: "
                        + msg.getResponseBody().length()
                        + "\r\n");
        return msg;
    }
}
