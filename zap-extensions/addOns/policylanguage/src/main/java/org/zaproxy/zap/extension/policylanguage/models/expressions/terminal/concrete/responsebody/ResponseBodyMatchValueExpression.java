package org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.concrete.responsebody;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.AbstractMatchValueTerminalExpression;

public class ResponseBodyMatchValueExpression extends AbstractMatchValueTerminalExpression {
    public ResponseBodyMatchValueExpression(String value) {
        super(value);
    }

    @Override
    public String getRelevantValue(HttpMessage msg) {
        return msg.getResponseBody().toString();
    }
}
