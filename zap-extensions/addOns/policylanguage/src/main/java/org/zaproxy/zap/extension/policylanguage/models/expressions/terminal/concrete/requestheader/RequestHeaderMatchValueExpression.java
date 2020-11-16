package org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.concrete.requestheader;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.AbstractMatchValueTerminalExpression;

public class RequestHeaderMatchValueExpression extends AbstractMatchValueTerminalExpression {
    public RequestHeaderMatchValueExpression(String value) {
        super(value);
    }

    @Override
    public String getRelevantValue(HttpMessage msg) {
        return msg.getRequestHeader().getHeadersAsString();
    }
}
