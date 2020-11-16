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
package org.zaproxy.zap.extension.policyverifier.models.expressions.terminal;

import org.parosproxy.paros.network.HttpMessage;

public abstract class AbstractMatchListTerminalExpression extends AbstractTerminalExpression {

    private String[] values;

    public AbstractMatchListTerminalExpression(String[] values) {
        super();
        this.values = values;
    }

    @Override
    public boolean interpret(HttpMessage msg) {
        for (String value : values) {
            if (!getRelevantValue(msg).contains(value)) return false;
        }
        return true;
    }
}
