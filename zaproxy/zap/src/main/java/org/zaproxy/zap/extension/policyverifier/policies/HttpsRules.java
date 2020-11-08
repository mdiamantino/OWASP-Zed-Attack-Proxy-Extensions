package org.zaproxy.zap.extension.policyverifier.policies;

import net.htmlparser.jericho.Source;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

public class HttpsRules implements Rule {
    private String name = "EnsureHttps";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(HttpMessage httpMessage) {
        return httpMessage.getRequestHeader().isSecure();
    }
}
