package org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.concrete.requestheader;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.AbstractMatchListTerminalExpression;

public class RequestHeaderMatchListExpression extends AbstractMatchListTerminalExpression {
    public RequestHeaderMatchListExpression(String[] values) {
        super(values);
    }

    @Override
    public String getRelevantValue(HttpMessage msg) {
        return msg.getRequestHeader().getHeadersAsString();
    }
}
