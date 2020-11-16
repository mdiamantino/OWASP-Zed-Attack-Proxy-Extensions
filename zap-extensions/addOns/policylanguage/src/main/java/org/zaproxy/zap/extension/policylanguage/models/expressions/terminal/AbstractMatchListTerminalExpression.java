package org.zaproxy.zap.extension.policylanguage.models.expressions.terminal;

import org.parosproxy.paros.network.HttpMessage;

public abstract class AbstractMatchListTerminalExpression extends AbstractTerminalExpression {

    private String[] values;

    public AbstractMatchListTerminalExpression(String[] values) {
        super();
        this.values = values;
    }

    @Override
    public boolean interpret(HttpMessage msg) {
        for (String value : values) {
            if (!getRelevantValue(msg).contains(value))
                return false;
        }
        return true;
    }
}
