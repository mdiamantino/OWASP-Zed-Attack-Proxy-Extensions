package org.zaproxy.zap.extension.policyverifier.policies;

import net.htmlparser.jericho.Source;
import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

import java.util.List;

/**
 * Definition of Missing Content-Type Header
 * Source: https://www.netsparker.com/web-vulnerability-scanner/vulnerabilities/missing-content-type-header/
 * A missing Content-Type header which means that this website could be
 * at risk of a MIME-sniffing attacks.
 * MIME type sniffing is a standard functionality in browsers to find
 * an appropriate way to render data where the HTTP headers sent
 * by the server are either inconclusive or missing.
 * This allows older versions of Internet Explorer and Chrome
 * to perform MIME-sniffing on the response body, potentially causing
 * the response body to be interpreted and displayed as a content type
 * other than the intended content type.
 */
public class MissingContentTypeHeader implements Rule {
    private final String NAME = "MissingContentTypeHeader";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isValid(HttpMessage httpMessage, Source source) {
        boolean contentTypeMissing = true;
        if (httpMessage.getResponseBody().length() > 0 && httpMessage.getResponseHeader().isHtml()) {
            List<String> contentType = httpMessage.getResponseHeader().getHeaderValues(HttpHeader.CONTENT_TYPE);
            contentTypeMissing = contentType.isEmpty() || contentType.contains("");
        }
        return contentTypeMissing;
    }


}
