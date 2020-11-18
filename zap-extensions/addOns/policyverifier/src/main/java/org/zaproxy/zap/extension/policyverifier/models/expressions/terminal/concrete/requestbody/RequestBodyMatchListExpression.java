package org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestbody;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.AbstractMatchListTerminalExpression;

import java.util.List;

public class RequestBodyMatchListExpression extends AbstractMatchListTerminalExpression {
    public RequestBodyMatchListExpression(List<String> values) {
        super(values);
    }

    @Override
    public String getRelevantValue(HttpMessage msg) {
        return msg.getRequestBody().toString();
    }
}
