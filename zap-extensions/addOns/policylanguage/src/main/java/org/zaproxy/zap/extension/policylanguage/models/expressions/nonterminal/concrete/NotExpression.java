package org.zaproxy.zap.extension.policylanguage.models.expressions.nonterminal.concrete;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policylanguage.models.expressions.Expression;
import org.zaproxy.zap.extension.policylanguage.models.expressions.nonterminal.AbstractCompoundNonTerminalExpression;

public class NotExpression extends AbstractCompoundNonTerminalExpression {

    public NotExpression(Expression leftExpression) {
        super(leftExpression, null);
    }

    @Override
    public boolean interpret(HttpMessage msg) {
        return !leftExpression.interpret(msg);
    }
}
