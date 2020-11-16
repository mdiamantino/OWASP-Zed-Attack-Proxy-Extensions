package org.zaproxy.zap.extension.policylanguage.models.expressions.nonterminal.concrete;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policylanguage.models.expressions.Expression;
import org.zaproxy.zap.extension.policylanguage.models.expressions.nonterminal.AbstractCompoundNonTerminalExpression;

public class AndExpression extends AbstractCompoundNonTerminalExpression {

    public AndExpression(Expression leftExpression, Expression rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public boolean interpret(HttpMessage msg) {
        return leftExpression.interpret(msg) && rightExpression.interpret(msg);
    }
}
