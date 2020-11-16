package org.zaproxy.zap.extension.policylanguage.models.expressions.nonterminal.concrete;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policylanguage.models.expressions.Expression;
import org.zaproxy.zap.extension.policylanguage.models.expressions.nonterminal.AbstractCompoundNonTerminalExpression;

public class OrExpression extends AbstractCompoundNonTerminalExpression {

    public OrExpression(Expression leftExpression, Expression rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public boolean interpret(HttpMessage msg) {
        return leftExpression.interpret(msg) || rightExpression.interpret(msg);
    }
}
