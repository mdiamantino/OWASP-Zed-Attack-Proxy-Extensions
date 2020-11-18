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
package org.zaproxy.zap.extension.policyverifier.controllers.txtLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.zaproxy.zap.extension.policyverifier.models.Policy;
import org.zaproxy.zap.extension.policyverifier.models.Rule;
import org.zaproxy.zap.extension.policyverifier.models.expressions.Expression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.RuleByExpression;

public class PolicyGeneratorFromTxt {
    File file;
    Pattern rulePattern = Pattern.compile("ruleName\\s*=\\s*(\\w*)\\s*,\\s*body\\s*=\\s*(.*);");

    public PolicyGeneratorFromTxt(File file) {
        this.file = file;
    }

    public String getFileName() {
        return FilenameUtils.removeExtension(file.getName());
    }

    public Policy generatePolicy() throws FileNotFoundException {
        String policyName = getFileName();
        Set<Rule> rules = getRules();
        return new Policy(rules, policyName);
    }

    public Set<Rule> getRules() throws FileNotFoundException {
        Set<Rule> rules = new HashSet<>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher m = rulePattern.matcher(line);
            if (m.find()) {
                String ruleName = m.group(1);
                String ruleExpression = m.group(2);
                Rule rule = buildRule(ruleName, ruleExpression);
                rules.add(rule);
            } else throw new IllegalArgumentException();
        }
        return rules;
    }

    private Rule buildRule(String ruleName, String ruleExpression) {
        RecursiveExpressionBuilder expr = new RecursiveExpressionBuilder(ruleExpression + "\r\n");
        Expression ruleExpr = expr.build();
        return new RuleByExpression(ruleExpr, ruleName);
    }
}
