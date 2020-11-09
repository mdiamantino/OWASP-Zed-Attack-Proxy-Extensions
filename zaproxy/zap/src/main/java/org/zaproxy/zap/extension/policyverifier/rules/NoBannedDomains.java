package org.zaproxy.zap.extension.policyverifier.rules;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NoBannedDomains implements Rule {
    private final List<String> BANNED_DOMAINS = new ArrayList<>(Arrays.asList("facebook.com", "twitter.com"));

    /**
     * Checks if the request is going to a domain in the list of banned domains.
     * @return false when the request's domain is in the banned domains list.
     */
    @Override
    public boolean isValid(HttpMessage msg) {
        String msgHostname = msg.getRequestHeader().getHostName();
        for (String domain : BANNED_DOMAINS) {
            if (msgHostname.equalsIgnoreCase(domain) ||
                    msgHostname.toLowerCase().endsWith("." + domain.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    // For testing purposes
    public List<String> getBANNED_DOMAINS() {
        return BANNED_DOMAINS;
    }
}
