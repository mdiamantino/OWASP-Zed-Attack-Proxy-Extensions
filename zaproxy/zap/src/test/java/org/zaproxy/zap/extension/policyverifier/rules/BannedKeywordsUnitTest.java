package org.zaproxy.zap.extension.policyverifier.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BannedKeywordsUnitTest {

    BannedKeywords rule;

    @BeforeEach
    public void setup() {
        rule = new BannedKeywords();
    }

    @Test
    public void isValid_NoBannedKeywordsInRequestBody_Valid() {
        HttpMessage msg = new HttpMessage();
        msg.setRequestBody("NO BANNED KEYWORDS IN THIS TEXT");
        assertTrue(rule.isValid(msg));
    }

    @Test
    public void isValid_BannedUppercaseKeywordInRequestBody_NotValid() {
        HttpMessage msg = new HttpMessage();
        Set<String> bannedKeywords = rule.getBANNED_KEYS();
        String bannedKeyword = new ArrayList<>(bannedKeywords).get(0);
        msg.setRequestBody(bannedKeyword.toUpperCase());
        assertFalse(rule.isValid(msg));
    }


    @Test
    public void isValid_BannedLowercaseKeywordInRequestBody_NotValid() {
        HttpMessage msg = new HttpMessage();
        Set<String> bannedKeywords = rule.getBANNED_KEYS();
        String bannedKeyword =  new ArrayList<>(bannedKeywords).get(0);
        msg.setRequestBody(bannedKeyword.toLowerCase());
    }
}
