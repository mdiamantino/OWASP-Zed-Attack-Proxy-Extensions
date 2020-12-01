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

import java.util.List;
import java.util.function.Function;
import org.apache.commons.lang.IncompleteArgumentException;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.expressions.Expression;

/**
 * Defines an Expression that is immediately evaluated without needing to go into other expressions
 */
public abstract class AbstractTerminalExpression implements Expression {
    private List<String> values;
    Function<HttpMessage, String> subjectLambda;

    public AbstractTerminalExpression() {
        super();
    }

    public void setSubjectAndValues(Subject subject, List<String> values) {
        this.values = values;
        subjectLambda = constructSubjectLambda(subject); // values must be set
    }

    /**
     * Specifies which part of the context needs to be interpreted
     *
     * @param msg the HttpMessage that has been sent in a particular call
     * @return the string containing the correct part of the context to evaluate
     */
    public String getRelevantValue(HttpMessage msg) {
        return subjectLambda.apply(msg);
    }

    protected List<String> getValues() {
        return values;
    }

    private Function<HttpMessage, String> constructSubjectLambda(Subject subject) {
        switch (subject) {
            case REQUEST_BODY:
                return (HttpMessage msg) -> msg.getRequestBody().toString();
            case REQUEST_HEADER:
                return (HttpMessage msg) -> msg.getRequestHeader().getHeader(consumeHeaderName());
            case RESPONSE_BODY:
                return (HttpMessage msg) -> msg.getResponseBody().toString();
            case RESPONSE_HEADER:
                return (HttpMessage msg) -> msg.getResponseHeader().getHeader(consumeHeaderName());
            default:
                throw new RuntimeException("Unknown subject" + subject);
        }
    }

    private String consumeHeaderName() {
        if (values.size() < 1) {
            throw new IncompleteArgumentException(
                    "Not enough arguments were provided to match against the header. (Must contain exactly 1 argument)");
        }

        String headerName = values.get(0);
        values.remove(0); // We consumed the header name
        return headerName;
    }
}
