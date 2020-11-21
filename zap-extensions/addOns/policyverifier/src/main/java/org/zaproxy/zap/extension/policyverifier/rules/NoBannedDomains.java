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
package org.zaproxy.zap.extension.policyverifier.rules;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoBannedDomains implements Rule {
    private final List<String> BANNED_DOMAINS =
            new ArrayList<>(Arrays.asList("facebook.com", "twitter.com"));

    /**
     * Checks if the request is going to a domain in the list of banned domains.
     *
     * @return false when the request's domain is in the banned domains list.
     */
    @Override
    public boolean isValid(HttpMessage msg) {
        String msgHostname = msg.getRequestHeader().getHostName();
        for (String domain : BANNED_DOMAINS) {
            if (msgHostname.equalsIgnoreCase(domain)
                    || msgHostname.toLowerCase().endsWith("." + domain.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    // For testing purposes
    public List<String> getBANNED_DOMAINS() {
        return BANNED_DOMAINS;
    }
}
