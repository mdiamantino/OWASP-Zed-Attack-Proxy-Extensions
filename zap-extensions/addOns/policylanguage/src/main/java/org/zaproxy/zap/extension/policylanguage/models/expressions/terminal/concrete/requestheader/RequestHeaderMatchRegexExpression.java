package org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.concrete.requestheader;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.AbstractMatchRegexTerminalExpression;

public class RequestHeaderMatchRegexExpression extends AbstractMatchRegexTerminalExpression {
    public RequestHeaderMatchRegexExpression(String pattern) {
        super(pattern);
    }

    @Override
    public String getRelevantValue(HttpMessage msg) {
        return msg.getRequestHeader().getHeadersAsString();
    }
}
