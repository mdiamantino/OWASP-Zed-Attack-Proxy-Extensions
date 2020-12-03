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

import java.util.regex.Pattern;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

public class NoEmails implements Rule {
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}", Pattern.CASE_INSENSITIVE);

    /**
     * Checks if the request contains a valid email address.
     *
     * @return false when an email address is included.
     */
    @Override
    public boolean isValid(HttpMessage msg) {
        boolean hasEmailInHeader =
                VALID_EMAIL_ADDRESS_REGEX
                        .matcher(msg.getRequestHeader().getHeadersAsString())
                        .find();
        boolean hasEmailInBody =
                VALID_EMAIL_ADDRESS_REGEX.matcher(msg.getRequestBody().toString()).find();
        return !hasEmailInHeader && !hasEmailInBody;
    }
}
