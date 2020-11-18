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
package org.zaproxy.zap.extension.policyverifier.controllers.txtLoader;

import java.util.ArrayList;
import java.util.List;
import org.zaproxy.zap.extension.policyverifier.models.expressions.Expression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.nonterminal.concrete.AndExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.nonterminal.concrete.NotExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.nonterminal.concrete.OrExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestheader.RequestHeaderMatchListExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestheader.RequestHeaderMatchRegexExpression;

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
        expression();
        return root;
    }

    private void expression() {
        term();
        while (symbol == OperatorEnum.OR) {
            OrExpression or = new OrExpression();
            or.setLeftExpression(root);
            term();
            or.setRightExpression(root);
            root = or;
        }
    }

    private void term() {
        factor();
        while (symbol == OperatorEnum.AND) {
            AndExpression and = new AndExpression();
            and.setLeftExpression(root);
            factor();
            and.setRightExpression(root);
            root = and;
        }
    }

    // TODO ADD OTHER CLASSES
    private void factor() {
        symbol = lexer.nextSymbol();
        if (symbol == OperatorEnum.MRQHL) {
            List<String> l = list();
            root = new RequestHeaderMatchListExpression(l);
            symbol = lexer.nextSymbol();
        } else if (symbol == OperatorEnum.MRQHR) {
            root = new RequestHeaderMatchRegexExpression("TODO");
            symbol = lexer.nextSymbol();
        } else if (symbol == OperatorEnum.NOT) {
            NotExpression not = new NotExpression();
            factor();
            not.setRightExpression(root);
            root = not;
        } else if (symbol == OperatorEnum.LEFT) {
            expression();
            symbol = lexer.nextSymbol();
        } else {
            throw new RuntimeException("Incorrect Expression");
        }
    }

    private List<String> list() {
        List<String> l = new ArrayList<String>();
        expect(OperatorEnum.LEFT_BR);
        symbol = lexer.nextSymbol();
        while (true) {
            expect(OperatorEnum.STRING);
            l.add(lexer.getString());

            symbol = lexer.nextSymbol();
            if (symbol == OperatorEnum.RIGHT_BR) break;
            expect(OperatorEnum.COMMA);
        }
        return l; // check
    }

    private void expect(OperatorEnum t) {
        symbol = lexer.nextSymbol();
        if (symbol != t) {
            throw new RuntimeException("Expected" + t);
        }
    }
}
