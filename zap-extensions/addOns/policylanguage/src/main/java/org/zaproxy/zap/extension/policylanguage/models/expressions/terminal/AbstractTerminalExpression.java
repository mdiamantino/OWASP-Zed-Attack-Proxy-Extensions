package org.zaproxy.zap.extension.policylanguage.models.expressions.terminal;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policylanguage.models.expressions.Expression;

/**
 * Defines an Expression that is immediately evaluated without needing to go into other expressions
 */
public abstract class AbstractTerminalExpression implements Expression {
    /**
     *
     * Specifies which part of the context needs to be interpreted
     *
     * @param msg the HttpMessage that has been sent in a particular call
     * @return the string containing the correct part of the context to evaluate
     */
    public abstract String getRelevantValue(HttpMessage msg);
}
