package org.zaproxy.zap.extension.policylanguage.models.expressions;

import org.parosproxy.paros.network.HttpMessage;

public interface Expression {
    /**
     *
     * Defines how to evaluate the validity of the provided context
     *
     * @param msg the HttpMessage that has been sent in a particular call
     * @return identifies whether the provided context abides by the specified expression's conditions
     */
    boolean interpret(HttpMessage msg);
}
