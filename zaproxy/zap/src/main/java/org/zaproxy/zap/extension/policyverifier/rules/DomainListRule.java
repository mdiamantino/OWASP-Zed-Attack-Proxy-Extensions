package org.zaproxy.zap.extension.policyverifier.rules;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.Rule;

public class DomainListRule implements Rule {
    public static final String[] domains = {
            "facebook.com",
            "twitter.com",
    };

    @Override
    public boolean isValid(HttpMessage msg) {
        String hostname = msg.getRequestHeader().getHostName();

        for (String domain : domains) {
            if (hostname.toLowerCase().contains(domain.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
