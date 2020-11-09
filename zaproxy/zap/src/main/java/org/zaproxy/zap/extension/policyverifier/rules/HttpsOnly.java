package org.zaproxy.zap.extension.policyverifier.rules;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

public class HttpsOnly implements Rule {

    /**
     * Checks if the request's protocol is HTTPS
     * @return false when the request's protocol is not HTTPS
     */
    @Override
    public boolean isValid(HttpMessage httpMessage) {
        return httpMessage.getRequestHeader().isSecure();
    }
}
