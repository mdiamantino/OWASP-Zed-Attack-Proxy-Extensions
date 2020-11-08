package org.zaproxy.zap.extension.policyverifier.rules;

import net.htmlparser.jericho.Source;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

import java.util.regex.Pattern;

public class EmailRule implements Rule {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}", Pattern.CASE_INSENSITIVE);


    @Override
    public boolean isValid(HttpMessage msg, Source src) {
        return isValid(msg);
    }

    public boolean isValid(HttpMessage msg) {
        boolean hasEmail = false;

        // Check body
        if (msg.getRequestHeader().isText())
        {
            hasEmail = VALID_EMAIL_ADDRESS_REGEX.matcher(msg.getRequestBody().toString()).find();
        }

        return !hasEmail;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
