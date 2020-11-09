package org.zaproxy.zap.extension.policyverifier.rules;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NoBannedKeywords implements Rule {
    private Set<String> BANNED_KEYS = new HashSet<>(Arrays.asList("SELECT", "DELETE"));

    /**
     * Checks if the request contains a keyword from a list of banned keys.
     * @return false when banned key is included.
     */
    @Override
    public boolean isValid(HttpMessage httpMessage) {
        if (httpMessage.getRequestBody().length() > 0) {
            String requestBody = httpMessage.getRequestBody().toString().toLowerCase();
            for (String word : BANNED_KEYS) {
                if (requestBody.contains(word.toLowerCase())) {
                    return false;
                }
            }
        }
        return true;
    }

    // For testing purposes only
    public Set<String> getBANNED_KEYS() {
        return BANNED_KEYS;
    }
}
