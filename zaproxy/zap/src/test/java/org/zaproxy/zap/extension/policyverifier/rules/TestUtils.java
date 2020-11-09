package org.zaproxy.zap.extension.policyverifier.rules;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {
    public static String buildDomainUrl(String domain) {
        return String.format("https://www.%s.com", domain);
    }

    static String buildResponseHeader(int responseLength) {
        return String.format("HTTP/1.1 200 OK\n" +
                "Content-Type: text/html; charset=utf-8\n" +
                "Server: Apache\n" +
                "Content-Length: %d\n", responseLength);
    }


    /**
     * Instantiate a set of mock Rules
     * @return Set of mock rules
     */
    public static Set<Rule> instantiateRules() {
        // Creating mock Rules
        NoCrossDomainScriptInclusion cdsiRule = mock(NoCrossDomainScriptInclusion.class);
        NoMissingContentTypeHeader mcthRule = mock(NoMissingContentTypeHeader.class);
        NoBannedKeywords bkRule = mock(NoBannedKeywords.class);
        HttpsOnly hoRule = mock(HttpsOnly.class);
        SecureCookie scRule = mock(SecureCookie.class);

        Set<Rule> rules = new HashSet<>();
        rules.add(cdsiRule);
        rules.add(mcthRule);
        rules.add(bkRule);
        rules.add(hoRule);
        rules.add(scRule);
        return rules;
    }

    /**
     * Select at random the rules which will be invalid
     * @param rules Complete set of rules
     * @param msg Som HttpMessage
     * @return Set of rules' names expected to have failed the validity test upon the message
     */
    public static Set<String> buildRandomBehaviourAndGetExpectedResults(Set<Rule> rules, HttpMessage msg) {
        Set<String> expectedInvalidRulesNames = new HashSet<>();
        Random random = new Random();
        for (Rule rule: rules) {
            Boolean willHttpMessageBeValidForRule = random.nextBoolean();
            when(rule.isValid(msg)).thenReturn(willHttpMessageBeValidForRule);
            if (!willHttpMessageBeValidForRule) {
                expectedInvalidRulesNames.add(rule.getName());
            }
        }
        return expectedInvalidRulesNames;
    }
}
