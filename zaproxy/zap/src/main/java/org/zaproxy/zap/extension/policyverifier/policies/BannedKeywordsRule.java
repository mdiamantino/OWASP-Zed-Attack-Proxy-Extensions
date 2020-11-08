package org.zaproxy.zap.extension.policyverifier.policies;

import net.htmlparser.jericho.Source;
import org.bouncycastle.util.Strings;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class BannedKeywordsRule implements Rule {
    private Set<String> BANNED_KEYS = new HashSet<>(Arrays.asList("SELECT", "DELETE"));
    private String name = "EnsureBannedKeywords";

    @Override
    public String getName() {
        return name;
    }

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

    /**
     * For testing purposes only
     * @return
     */
    public Set<String> getBANNED_KEYS() {
        return BANNED_KEYS;
    }
}
