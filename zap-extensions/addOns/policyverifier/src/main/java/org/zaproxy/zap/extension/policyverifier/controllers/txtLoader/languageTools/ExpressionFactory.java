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

    private static int HEADER_MIN_ARG_LENGTH = 2;
    private static int BODY_MIN_ARG_LENGTH = 1;

    static public Expression extractOperationFromSymbol(OperatorEnum symbol, List<String> l) throws IllegalArgumentException {
        Expression root;
        if (symbol == OperatorEnum.MRQHL) {
            assert l.size() >= HEADER_MIN_ARG_LENGTH;
            root = new RequestHeaderMatchListExpression(l);
        } else if (symbol == OperatorEnum.MRQHR) {
            assert l.size() == HEADER_MIN_ARG_LENGTH;
            root = new RequestHeaderMatchRegexExpression(l);
        } else if (symbol == OperatorEnum.MRSHL) {
            assert l.size() >= HEADER_MIN_ARG_LENGTH;
            root = new ResponseHeaderMatchListExpression(l);
        } else if (symbol == OperatorEnum.MRSHR) {
            assert l.size() == HEADER_MIN_ARG_LENGTH;
            root = new ResponseHeaderMatchRegexExpression(l);
        } else if (symbol == OperatorEnum.MRSBR) {
            assert l.size() == BODY_MIN_ARG_LENGTH;
            root = new ResponseBodyMatchRegexExpression(l);
        } else if (symbol == OperatorEnum.MRQBR) {
            assert l.size() == BODY_MIN_ARG_LENGTH;
            root = new RequestBodyMatchRegexExpression(l);
        } else {
            throw new IllegalArgumentException();
        }
        return root;
    }

}
