package org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestbody;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.AbstractMatchValueTerminalExpression;

public class RequestBodyMatchValueExpression extends AbstractMatchValueTerminalExpression {
    public RequestBodyMatchValueExpression(String value) {
        super(value);
    }

    @Override
    public String getRelevantValue(HttpMessage msg) {
        return msg.getRequestBody().toString();
    }
}
