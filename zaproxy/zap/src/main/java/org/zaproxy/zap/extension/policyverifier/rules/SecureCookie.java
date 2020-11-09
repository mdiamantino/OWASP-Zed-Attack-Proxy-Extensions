package org.zaproxy.zap.extension.policyverifier.rules;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

public class SecureCookie implements Rule {

    /**
     * Checks if the request containts "secure" cookies.
     * A secure cookie must have the have the HttpOnly,Secure,SameSite attributes set
     * @return false when cookies are not secure.
     */
    @Override
    public boolean isValid(HttpMessage httpMessage) {
        String cookieParams = httpMessage.getCookieParamsAsString();
        return cookieParams.isEmpty() || (cookieParams.contains("SameSite")
                && cookieParams.contains("HttpOnly")
                && cookieParams.contains("Secure"));
    }
}
