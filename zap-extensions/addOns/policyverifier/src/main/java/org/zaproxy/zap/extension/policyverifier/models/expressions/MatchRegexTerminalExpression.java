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
package org.zaproxy.zap.extension.policyverifier.models.expressions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.IncompleteArgumentException;
import org.parosproxy.paros.network.HttpMessage;

public class MatchRegexTerminalExpression extends AbstractTerminalExpression {
    @Override
    public void setSubjectAndValues(Subject subject, List<String> values) {
        super.setSubjectAndValues(subject, values);

        if (getValues().size() != 1)
            throw new IncompleteArgumentException("Expected exactly one argument: the regexp");
    }

    @Override
    public boolean test(HttpMessage msg) {
        String pattern = getValues().get(0);
        Pattern compiledPattern = Pattern.compile(pattern);

        String relevantValue = getRelevantValue(msg);

        if (relevantValue == null || relevantValue.isEmpty()) return true;

        Matcher matcher = compiledPattern.matcher(relevantValue);
        return matcher.find();
    }
}
