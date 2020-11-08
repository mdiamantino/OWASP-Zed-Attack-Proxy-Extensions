package org.zaproxy.zap.extension.policyverifier.rules;

import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

import java.util.List;

public class SecureCookie implements Rule {

    private String name = "EnsureSecureCookie";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(HttpMessage httpMessage) {
        String cookieParams = httpMessage.getCookieParamsAsString();
        return cookieParams.isEmpty() || (cookieParams.contains("SameSite")
                && cookieParams.contains("HttpOnly")
                && cookieParams.contains("Secure"));
    }
}
