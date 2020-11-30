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

import java.util.List;
import org.zaproxy.zap.extension.policyverifier.models.expressions.Expression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.AbstractTerminalExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.Subject;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.MatchListTerminalExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.MatchRegexTerminalExpression;

public class ExpressionFactory {
    public static boolean isTokenAnOperation(OperatorEnum op) {
        try {
            getOperationClass(op);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private static Class<? extends AbstractTerminalExpression> getOperationClass(
            OperatorEnum symbol) {
        Class<? extends AbstractTerminalExpression> operation;
        switch (symbol) {
            case matchList:
                return MatchListTerminalExpression.class;
            case matchRegex:
                return MatchRegexTerminalExpression.class;
            default:
                System.out.println("Unknown operation symbol" + symbol);
                throw new RuntimeException(
                        "Unknown operation symbol" + symbol); // todo throw something more relevant
        }
    }

    public static boolean isTokenASubject(OperatorEnum op) {
        try {
            getSubject(op);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private static Subject getSubject(OperatorEnum symbol) {
        Class<? extends AbstractTerminalExpression> operation;
        switch (symbol) // todo: might need to move this elsewhere
        {
            case REQUEST_BODY:
                return Subject.REQUEST_BODY;
            case REQUEST_HEADER:
                return Subject.REQUEST_HEADER;
            case RESPONSE_BODY:
                return Subject.RESPONSE_BODY;
            case RESPONSE_HEADER:
                return Subject.RESPONSE_HEADER;
            default:
                throw new RuntimeException("Unknown subject symbol" + symbol);
        }
    }

    public static Expression extractOperationFromSymbol(
            OperatorEnum symbol, OperatorEnum subjectSymbol, List<String> l) throws Exception {
        Subject subject = getSubject(subjectSymbol);
        return getOperationClass(symbol)
                .getDeclaredConstructor(Subject.class, List.class)
                .newInstance(subject, l);
    }
}
