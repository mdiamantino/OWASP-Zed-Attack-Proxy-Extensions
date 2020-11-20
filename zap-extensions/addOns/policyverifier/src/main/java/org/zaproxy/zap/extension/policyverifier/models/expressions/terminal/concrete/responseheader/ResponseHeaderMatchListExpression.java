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
package org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.responseheader;

import java.util.List;
import org.apache.commons.lang.IncompleteArgumentException;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.AbstractMatchListTerminalExpression;

public class ResponseHeaderMatchListExpression extends AbstractMatchListTerminalExpression {
    public ResponseHeaderMatchListExpression(List<String> values) {
        super(values);
        if (values.size() < 2)
            throw new IncompleteArgumentException(
                    "Not enough arguments were provided to match against the header. (Min 2 arguments)");
    }

    @Override
    public String getRelevantValue(HttpMessage msg) {
        String headerName = getValues().get(0);
        return msg.getRequestHeader().getHeader(headerName);
    }
}
