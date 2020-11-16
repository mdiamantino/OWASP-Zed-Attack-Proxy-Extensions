package org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.concrete.responseheader;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.AbstractMatchValueTerminalExpression;

public class ResponseHeaderMatchValueExpression extends AbstractMatchValueTerminalExpression {
    public ResponseHeaderMatchValueExpression(String value) {
        super(value);
    }

    @Override
    public String getRelevantValue(HttpMessage msg) {
        return msg.getResponseHeader().getHeadersAsString();
    }
}
