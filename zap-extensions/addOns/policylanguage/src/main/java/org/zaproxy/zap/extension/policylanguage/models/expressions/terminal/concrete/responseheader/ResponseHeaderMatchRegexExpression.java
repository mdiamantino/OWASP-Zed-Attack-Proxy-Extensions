package org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.concrete.responseheader;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.AbstractMatchRegexTerminalExpression;

public class ResponseHeaderMatchRegexExpression extends AbstractMatchRegexTerminalExpression {
    public ResponseHeaderMatchRegexExpression(String pattern) {
        super(pattern);
    }

    @Override
    public String getRelevantValue(HttpMessage msg) {
        return msg.getResponseHeader().getHeadersAsString();
    }
}
