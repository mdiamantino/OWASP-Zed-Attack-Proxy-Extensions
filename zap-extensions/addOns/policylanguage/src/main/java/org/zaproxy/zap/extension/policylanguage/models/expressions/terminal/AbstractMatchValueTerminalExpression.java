package org.zaproxy.zap.extension.policylanguage.models.expressions.terminal;

import org.parosproxy.paros.network.HttpMessage;

public abstract class AbstractMatchValueTerminalExpression extends  AbstractTerminalExpression {
    private String value;

    public AbstractMatchValueTerminalExpression(String value) {
        this.value = value;
    }

    @Override
    public boolean interpret(HttpMessage msg) {
        return getRelevantValue(msg).contains(value);
    }
}
