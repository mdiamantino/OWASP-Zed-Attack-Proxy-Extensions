package org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.concrete.responseheader;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.AbstractMatchListTerminalExpression;

public class ResponseHeaderMatchListExpression extends AbstractMatchListTerminalExpression {
    public ResponseHeaderMatchListExpression(String[] values) {
        super(values);
    }

    @Override
    public String getRelevantValue(HttpMessage msg) {
        return msg.getResponseHeader().getHeadersAsString();
    }
}
