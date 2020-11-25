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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zaproxy.zap.extension.policyverifier.models.expressions.Expression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.RequestBodyMatchRegexExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.RequestHeaderMatchListExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.RequestHeaderMatchRegexExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.ResponseBodyMatchRegexExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.ResponseHeaderMatchListExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.ResponseHeaderMatchRegexExpression;

public class ExpressionFactory {
    private static final Map<OperatorEnum, Class<? extends Expression>> operations =
            new HashMap<OperatorEnum, Class<? extends Expression>>() {
                private static final long serialVersionUID = -1113582265865921787L;

                {
                    put(OperatorEnum.MRQHL, RequestHeaderMatchListExpression.class);
                    put(OperatorEnum.MRQHR, RequestHeaderMatchRegexExpression.class);
                    put(OperatorEnum.MRSHL, ResponseHeaderMatchListExpression.class);
                    put(OperatorEnum.MRSHR, ResponseHeaderMatchRegexExpression.class);
                    put(OperatorEnum.MRQBR, RequestBodyMatchRegexExpression.class);
                    put(OperatorEnum.MRSBR, ResponseBodyMatchRegexExpression.class);
                }
            };

    public static boolean isTokenAnOperation(OperatorEnum op) {
        return operations.containsKey(op);
    }

    public static Expression extractOperationFromSymbol(OperatorEnum symbol, List<String> l) throws Exception {
        return operations.get(symbol).getDeclaredConstructor(List.class).newInstance(l);
    }
}
