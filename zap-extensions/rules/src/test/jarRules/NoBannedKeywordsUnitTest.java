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
package org.zaproxy.zap.extension.policyverifier.models.jarRules;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;

public class NoBannedKeywordsUnitTest {

    NoBannedKeywords rule;

    @BeforeEach
    public void setup() {
        rule = new NoBannedKeywords();
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
        String bannedKeyword = new ArrayList<>(bannedKeywords).get(0);
        msg.setRequestBody(bannedKeyword.toLowerCase());
    }
}
