/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2020 The ZAP Development Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zaproxy.zap.extension.policyverifier.controllers.txtLoader.languageTools;

import org.zaproxy.zap.extension.policyverifier.models.expressions.Expression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.nonterminal.concrete.AndExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.nonterminal.concrete.NotExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.nonterminal.concrete.OrExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestbody.RequestBodyMatchRegexExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestheader.RequestHeaderMatchListExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestheader.RequestHeaderMatchRegexExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.responsebody.ResponseBodyMatchRegexExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.responseheader.ResponseHeaderMatchListExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.responseheader.ResponseHeaderMatchRegexExpression;

import java.util.ArrayList;
import java.util.List;

public class RecursiveExpressionBuilder {
    // Structural components
    private Expression root;
    private OperatorEnum symbol;

    // Helper
    private final Lexer lexer;

    public RecursiveExpressionBuilder(String expression) {
        this.lexer = new Lexer(expression);
    }

    public Expression build() {
        parseOrExpressionAndInside();
        return root;
    }

    private void parseOrExpressionAndInside() {
        parseAndExpressionAndInside();
        while (symbol == OperatorEnum.OR) {
            OrExpression or = new OrExpression();
            or.setLeftExpression(root);
            parseAndExpressionAndInside();
            or.setRightExpression(root);
            root = or;
        }
    }

    private void parseAndExpressionAndInside() {
        parseTerminalExpressionOrANot();
        while (symbol == OperatorEnum.AND) {
            AndExpression and = new AndExpression();
            and.setLeftExpression(root);
            parseTerminalExpressionOrANot();
            and.setRightExpression(root);
            root = and;
        }
    }

    private void parseTerminalExpressionOrANot() {
        symbol = lexer.nextSymbol();
        try {
            List<String> l = list();
            root = ExpressionFactory.extractOperationFromSymbol(symbol, l);
            lexer.nextSymbol();
        } catch (IllegalArgumentException e) {
            if (symbol == OperatorEnum.NOT) {
                NotExpression not = new NotExpression();
                parseTerminalExpressionOrANot();
                not.setLeftExpression(root);
                root = not;
            } else if (symbol == OperatorEnum.LEFT) {
                parseOrExpressionAndInside();
                symbol = lexer.nextSymbol();
            } else {
                throw new RuntimeException("Incorrect Expression");
            }
        }
    }

//    private boolean extractOperationFromSymbol() {
//        boolean isSymbolAssociatedWithTerminalExpression = true;
//        List<String> l = list();
//        if (symbol == OperatorEnum.MRQHL) {
//            root = new RequestHeaderMatchListExpression(l);
//        } else if (symbol == OperatorEnum.MRQHR) {
//            assert l.size() == 1;
//            root = new RequestHeaderMatchRegexExpression(l.get(0));
//        } else if (symbol == OperatorEnum.MRSHL) {
//            root = new ResponseHeaderMatchListExpression(l);
//        } else if (symbol == OperatorEnum.MRSHR) {
//            assert l.size() == 1;
//            root = new ResponseHeaderMatchRegexExpression(l.get(0));
//        } else if (symbol == OperatorEnum.MRSBR) {
//            assert l.size() == 1;
//            root = new ResponseBodyMatchRegexExpression(l.get(0));
//        } else if (symbol == OperatorEnum.MRQBR) {
//            assert l.size() == 1;
//            root = new RequestBodyMatchRegexExpression(l.get(0));
//        } else {
//            isSymbolAssociatedWithTerminalExpression = false;
//        }
//        return isSymbolAssociatedWithTerminalExpression;
//    }

    private List<String> list() {
        List<String> l = new ArrayList<>();
        expect(OperatorEnum.LEFT_BR);
        while (true) {
            expect(OperatorEnum.STRING);
            l.add(lexer.getString());
            symbol = lexer.nextSymbol();
            if (symbol == OperatorEnum.RIGHT_BR) break;
            else if (symbol != OperatorEnum.COMMA)
                throw new RuntimeException("Expected" + OperatorEnum.COMMA);
        }
        return l;
    }

    private void expect(OperatorEnum t) {
        symbol = lexer.nextSymbol();
        if (symbol != t) {
            throw new RuntimeException("Expected" + t);
        }
    }
}
