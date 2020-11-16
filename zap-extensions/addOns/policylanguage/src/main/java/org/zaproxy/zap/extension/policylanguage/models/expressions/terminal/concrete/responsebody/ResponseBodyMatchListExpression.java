package org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.concrete.responsebody;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policylanguage.models.expressions.terminal.AbstractMatchListTerminalExpression;

public class ResponseBodyMatchListExpression extends AbstractMatchListTerminalExpression {
    public ResponseBodyMatchListExpression(String[] values) {
        super(values);
    }

    @Override
    public String getRelevantValue(HttpMessage msg) {
        return msg.getResponseBody().toString();
    }
}
