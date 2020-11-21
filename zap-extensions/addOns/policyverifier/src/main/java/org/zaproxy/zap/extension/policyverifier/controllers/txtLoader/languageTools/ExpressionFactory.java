package org.zaproxy.zap.extension.policyverifier.controllers.txtLoader.languageTools;

import org.zaproxy.zap.extension.policyverifier.models.expressions.Expression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestbody.RequestBodyMatchRegexExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestheader.RequestHeaderMatchListExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestheader.RequestHeaderMatchRegexExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.responsebody.ResponseBodyMatchRegexExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.responseheader.ResponseHeaderMatchListExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.responseheader.ResponseHeaderMatchRegexExpression;

import java.util.List;

public class ExpressionFactory {

    static public Expression extractOperationFromSymbol(OperatorEnum symbol, List<String> l) throws IllegalArgumentException {
        Expression root;
        if (symbol == OperatorEnum.MRQHL) {
            root = new RequestHeaderMatchListExpression(l);
        } else if (symbol == OperatorEnum.MRQHR) {
            assert l.size() == 1;
            root = new RequestHeaderMatchRegexExpression(l.get(0));
        } else if (symbol == OperatorEnum.MRSHL) {
            root = new ResponseHeaderMatchListExpression(l);
        } else if (symbol == OperatorEnum.MRSHR) {
            assert l.size() == 1;
            root = new ResponseHeaderMatchRegexExpression(l.get(0));
        } else if (symbol == OperatorEnum.MRSBR) {
            assert l.size() == 1;
            root = new ResponseBodyMatchRegexExpression(l.get(0));
        } else if (symbol == OperatorEnum.MRQBR) {
            assert l.size() == 1;
            root = new RequestBodyMatchRegexExpression(l.get(0));
        } else {
            throw new IllegalArgumentException();
        }
        return root;
    }

}
