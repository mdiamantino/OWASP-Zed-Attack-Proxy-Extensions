package org.zaproxy.zap.extension.policyverifier.rules;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

public class HttpsOnly implements Rule {

    @Override
    public boolean isValid(HttpMessage httpMessage) {
        return httpMessage.getRequestHeader().isSecure();
    }
}
