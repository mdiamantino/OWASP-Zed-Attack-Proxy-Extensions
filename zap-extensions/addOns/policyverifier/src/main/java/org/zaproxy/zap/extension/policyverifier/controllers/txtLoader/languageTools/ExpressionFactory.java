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
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestbody.RequestBodyMatchRegexExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestheader.RequestHeaderMatchListExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.requestheader.RequestHeaderMatchRegexExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.responsebody.ResponseBodyMatchRegexExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.responseheader.ResponseHeaderMatchListExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.responseheader.ResponseHeaderMatchRegexExpression;

public class ExpressionFactory {

    private static int HEADER_MIN_ARG_LENGTH = 2;
    private static int BODY_MIN_ARG_LENGTH = 1;

    public static Expression extractOperationFromSymbol(OperatorEnum symbol, List<String> l)
            throws IllegalArgumentException {
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
