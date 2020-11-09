package org.zaproxy.zap.extension.policyverifier.rules;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

import java.util.regex.Pattern;

public class NoEmails implements Rule {
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isValid(HttpMessage msg) {
        boolean hasEmailInHeader = VALID_EMAIL_ADDRESS_REGEX.matcher(msg.getRequestHeader().getHeadersAsString()).find();
        boolean hasEmailInBody = VALID_EMAIL_ADDRESS_REGEX.matcher(msg.getRequestBody().toString()).find();
        return !hasEmailInHeader && !hasEmailInBody;
    }
}
