package org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.concrete.responsebody;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.AbstractMatchRegexTerminalExpression;

public class ResponseBodyMatchRegexExpression extends AbstractMatchRegexTerminalExpression {
    public ResponseBodyMatchRegexExpression(String pattern) {
        super(pattern);
    }

    @Override
    public String getRelevantValue(HttpMessage msg) {
        return msg.getResponseBody().toString();
    }
}
