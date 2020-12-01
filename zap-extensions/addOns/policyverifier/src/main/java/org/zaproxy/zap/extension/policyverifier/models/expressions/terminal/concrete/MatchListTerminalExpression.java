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
package org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete;

import java.util.List;
import org.apache.commons.lang.IncompleteArgumentException;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.AbstractTerminalExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.Subject;

public class MatchListTerminalExpression extends AbstractTerminalExpression {

    @Override
    public void setSubjectAndValues(Subject subject, List<String> values) {
        super.setSubjectAndValues(subject, values);

        if (values.size() < 1)
            throw new IncompleteArgumentException( // todo
                    "Not enough arguments were provided to match against the header. (Must contain at least 1 argument)");
    }

    @Override
    public boolean interpret(HttpMessage msg) {
        List<String> values = getValues();
        String relevantValue = getRelevantValue(msg);
        if (relevantValue == null || relevantValue.isEmpty()) return true;
        for (int i = 1; i < values.size(); i++) {
            if (!relevantValue.contains(values.get(i))) return false;
        }
        return true;
    }
}
