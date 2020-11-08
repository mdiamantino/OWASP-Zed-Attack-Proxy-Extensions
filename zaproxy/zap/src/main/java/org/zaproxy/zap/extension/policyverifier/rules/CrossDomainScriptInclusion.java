package org.zaproxy.zap.extension.policyverifier.rules;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

/**
 * Definition of Cross-domain script include
 * Source: https://portswigger.net/kb/issues/00500500_cross-domain-script-include
 * When an application includes a script from an external domain,
 * this script is executed by the browser within the security context
 * of the invoking application. The script can therefore do anything
 * that the application's own scripts can do, such as accessing
 * application data and performing actions within the context of the current user.
 */
public class CrossDomainScriptInclusion implements Rule {
    private final String NAME = "CrossDomainScriptInclusion";

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

    @Override
    public String getName() {
        return NAME;
    }
}
