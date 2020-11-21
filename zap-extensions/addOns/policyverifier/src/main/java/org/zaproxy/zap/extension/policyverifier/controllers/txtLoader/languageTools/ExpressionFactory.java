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

    public static Expression extractOperationFromSymbol(OperatorEnum symbol, List<String> l)
            throws IllegalArgumentException {
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
