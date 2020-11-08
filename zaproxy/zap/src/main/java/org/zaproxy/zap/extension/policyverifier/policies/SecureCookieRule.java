package org.zaproxy.zap.extension.policyverifier.policies;

import net.htmlparser.jericho.Source;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

public class SecureCookieRule implements Rule {

    private String name = "EnsureSecureCookie";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(HttpMessage httpMessage) {
        String cookieParams = httpMessage.getCookieParamsAsString();
        return cookieParams.contains("SameSite")
                && cookieParams.contains("HttpOnly")
                && cookieParams.contains("Secure");
    }
}
