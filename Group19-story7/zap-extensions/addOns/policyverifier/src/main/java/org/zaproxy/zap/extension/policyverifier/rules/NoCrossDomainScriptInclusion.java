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

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

/**
 * Definition of Cross-domain script include Source:
 * https://portswigger.net/kb/issues/00500500_cross-domain-script-include When an application
 * includes a script from an external domain, this script is executed by the browser within the
 * security context of the invoking application. The script can therefore do anything that the
 * application's own scripts can do, such as accessing application data and performing actions
 * within the context of the current user.
 */
public class NoCrossDomainScriptInclusion implements Rule {

    /**
     * Checks if the request contains a cross-domain script.
     *
     * @return false when a cross-domain script is included.
     */
    @Override
    public boolean isValid(HttpMessage msg) {
        if (msg.getResponseBody().length() > 0 && msg.getResponseHeader().isHtml()) {
            Source source = new Source(msg.getResponseBody().toString());
            for (Element elem : source.getAllElements(HTMLElementName.SCRIPT)) {
                String scriptUrl = elem.getAttributeValue("src");
                String originalHost = msg.getRequestHeader().getHostName();
                if (scriptUrl != null && scriptUrl.startsWith("http")) {
                    try {
                        URI scriptURI = new URI(scriptUrl, true);
                        // If domains are different, then the rule is not valid
                        if (!scriptURI.getHost().toLowerCase().equals(originalHost.toLowerCase())) {
                            return false;
                        }
                    } catch (URIException e) {
                        // Ignore
                    }
                }
            }
        }
        return true;
    }
}
