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

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.zaproxy.zap.extension.policyverifier.models.expressions.Expression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.nonterminal.concrete.AndExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.nonterminal.concrete.NotExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.nonterminal.concrete.OrExpression;

public class RecursiveExpressionBuilder {
    private static final Logger logger = Logger.getLogger(RecursiveExpressionBuilder.class);
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
        if (ExpressionFactory.checkIfIsOperation(symbol)) {
            OperatorEnum operationSymbol = symbol;
            List<String> l = list();
            root = ExpressionFactory.extractOperationFromSymbol(operationSymbol, l);
            symbol = lexer.nextSymbol();
        } else if (symbol == OperatorEnum.NOT) {
            NotExpression not = new NotExpression();
            parseTerminalExpressionOrANot();
            not.setLeftExpression(root);
            root = not;
        } else if (symbol == OperatorEnum.LEFT) {
            parseOrExpressionAndInside();
            expect(OperatorEnum.RIGHT);
            symbol = lexer.nextSymbol();
        } else {
            throw new RuntimeException("Incorrect Expression: " + symbol);
        }
    }

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
