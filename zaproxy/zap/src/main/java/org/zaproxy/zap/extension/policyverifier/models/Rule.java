package org.zaproxy.zap.extension.policyverifier.models;

import net.htmlparser.jericho.Source;
import org.parosproxy.paros.network.HttpMessage;

/**
 * A Rule is an entity which can be valid or not according to the checked HttpMessage
 */
public interface Rule {
    String getName();

    /**
     * The implementation of this method embeds the algorithm to check if the rule is valid or not.
     *
     * @param msg HttpMessage to check against validity
     * @return Boolean telling if the Http message is follows this rule
     */
    boolean isValid(HttpMessage msg);
}