package org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestbody;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.AbstractMatchRegexTerminalExpression;

public class RequestBodyMatchRegexExpression extends AbstractMatchRegexTerminalExpression {
    public RequestBodyMatchRegexExpression(String pattern) {
        super(pattern);
    }

    @Override
    public String getRelevantValue(HttpMessage msg) {
        return msg.getRequestBody().toString();
    }
}
