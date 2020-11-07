package org.zaproxy.zap.extension.policyverifier.rules;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;
import net.htmlparser.jericho.Source;


public class DomainListRule implements Rule {
    public static final String[] DOMAINS = {
            "facebook.com",
            "twitter.com",
    };

    private String[] domains;

    DomainListRule(String[] domains) {
        this.domains = domains;
    }

    DomainListRule() {
        this(DOMAINS);
    }

    @Override
    public boolean isValid(HttpMessage msg, Source src) {
        return isValid(msg);
    }

    public boolean isValid(HttpMessage msg) {
        String hostname = msg.getRequestHeader().getHostName();

        System.out.println("hostname is: " + hostname);

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
