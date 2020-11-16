package org.zaproxy.zap.extension.policylanguage.models.expressions.nonterminal;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policylanguage.models.expressions.Expression;

/**
 * These expressions need to evaluate multiple expressions before the final interpretation can be returned.
 */
public abstract class AbstractCompoundNonTerminalExpression implements Expression {
    protected Expression leftExpression;
    protected Expression rightExpression;

    protected AbstractCompoundNonTerminalExpression(Expression leftExpression, Expression rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }
}
