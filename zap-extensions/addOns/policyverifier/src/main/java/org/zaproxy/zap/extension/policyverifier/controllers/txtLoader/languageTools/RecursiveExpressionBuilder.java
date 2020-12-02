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
import java.util.function.Predicate;
import org.apache.log4j.Logger;
import org.parosproxy.paros.network.HttpMessage;

public class RecursiveExpressionBuilder {
    // Structural components
    private OperatorEnum symbol;
    private Predicate<HttpMessage> pred;

    // Helper
    private final Lexer lexer;

    public RecursiveExpressionBuilder(String expression) {
        this.lexer = new Lexer(expression);
    }

    public Predicate<HttpMessage> build() {
        parseOrExpressionAndInside();
        return pred;
    }

    private void parseOrExpressionAndInside() {
        parseAndExpressionAndInside();
        while (symbol == OperatorEnum.OR) {
            Predicate<HttpMessage> predCur = pred;
            parseAndExpressionAndInside();
            System.out.println("cur = " + predCur + "new= " + pred);
            pred = predCur.or(pred);
        }
    }

    private void parseAndExpressionAndInside() {
        parseTerminalExpressionOrANot();
        while (symbol == OperatorEnum.AND) {
            Predicate<HttpMessage> predCur = pred;
            parseTerminalExpressionOrANot();
            pred = predCur.and(pred);
        }
    }

    private void parseTerminalExpressionOrANot() {
        symbol = lexer.nextSymbol();
        if (ExpressionFactory.isTokenAnOperation(symbol)) {
            OperatorEnum operationSymbol = symbol;
            OperatorEnum subjectSymbol = lexer.nextSymbol();
            if (!ExpressionFactory.isTokenASubject(subjectSymbol)) {
                throw new RuntimeException("Expected a subject");
            }

            List<String> l = extractOperationArgumentList();
            pred = ExpressionFactory.extractOperationFromSymbol(operationSymbol, subjectSymbol, l);

            System.out.println("Finished with expression: " + pred);
            symbol = lexer.nextSymbol();
        } else if (symbol == OperatorEnum.NOT) {
            parseTerminalExpressionOrANot();
            pred = pred.negate();
        } else if (symbol == OperatorEnum.LEFT) {
            parseOrExpressionAndInside();
            expect(OperatorEnum.RIGHT);
            symbol = lexer.nextSymbol();
        } else {
            throw new RuntimeException("Incorrect Expression: " + symbol);
        }

        assert pred != null;
    }

    private List<String> extractOperationArgumentList() {
        List<String> l = new ArrayList<>();
        expect(OperatorEnum.LEFT_BR);
        while (true) {
            expect(OperatorEnum.STRING);
            l.add(lexer.getString());
            symbol = lexer.nextSymbol();
            if (symbol == OperatorEnum.RIGHT_BR) break;
            else if (symbol != OperatorEnum.COMMA)
                throw new RuntimeException("Expected" + OperatorEnum.COMMA + "but got " + symbol);
        }
        return l;
    }

    private void expect(OperatorEnum t) {
        symbol = lexer.nextSymbol();
        if (symbol != t) {
            throw new RuntimeException("Expected " + t + " but got " + symbol);
        }
    }
}
