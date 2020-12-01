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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

public class NoBannedKeywords implements Rule {
    private Set<String> BANNED_KEYS = new HashSet<>(Arrays.asList("SELECT", "DELETE"));

    /**
     * Checks if the request contains a keyword from a list of banned keys.
     *
     * @return false when banned key is included.
     */
    @Override
    public boolean isValid(HttpMessage httpMessage) {
        if (httpMessage.getRequestBody().length() > 0) {
            String requestBody = httpMessage.getRequestBody().toString().toLowerCase();
            for (String word : BANNED_KEYS) {
                if (requestBody.contains(word.toLowerCase())) {
                    return false;
                }
            }
        }
        return true;
    }

    // For testing purposes only
    public Set<String> getBANNED_KEYS() {
        return BANNED_KEYS;
    }
}
