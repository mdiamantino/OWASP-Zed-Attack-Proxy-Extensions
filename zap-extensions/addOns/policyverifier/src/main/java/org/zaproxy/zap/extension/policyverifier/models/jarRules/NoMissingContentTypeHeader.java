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

import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

import java.util.List;

/**
 * Definition of Missing Content-Type Header Source:
 * https://www.netsparker.com/web-vulnerability-scanner/vulnerabilities/missing-content-type-header/
 * A missing Content-Type header which means that this website could be at risk of a MIME-sniffing
 * attacks. MIME type sniffing is a standard functionality in browsers to find an appropriate way to
 * render data where the HTTP headers sent by the server are either inconclusive or missing. This
 * allows older versions of Internet Explorer and Chrome to perform MIME-sniffing on the response
 * body, potentially causing the response body to be interpreted and displayed as a content type
 * other than the intended content type.
 */
public class NoMissingContentTypeHeader implements Rule {

    /**
     * Checks if the request contains a Content-Type header.
     *
     * @return false when a Content-Type header is not included.
     */
    @Override
    public boolean isValid(HttpMessage httpMessage) {
        boolean contentTypeMissing = false;
        if (httpMessage.getResponseBody().length() > 0) {
            List<String> contentType =
                    httpMessage.getResponseHeader().getHeaderValues(HttpHeader.CONTENT_TYPE);
            contentTypeMissing = contentType.isEmpty() || contentType.contains("");
        }
        return !contentTypeMissing;
    }
}
