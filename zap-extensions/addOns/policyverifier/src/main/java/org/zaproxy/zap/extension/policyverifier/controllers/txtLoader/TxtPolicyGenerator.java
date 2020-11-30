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

import org.apache.log4j.Logger;
import org.zaproxy.zap.extension.policyverifier.controllers.PolicyGenerator;
import org.zaproxy.zap.extension.policyverifier.controllers.txtLoader.languageTools.RecursiveExpressionBuilder;
import org.zaproxy.zap.extension.policyverifier.models.Policy;
import org.zaproxy.zap.extension.policyverifier.models.Rule;
import org.zaproxy.zap.extension.policyverifier.models.expressions.Expression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.RuleByExpression;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtPolicyGenerator implements PolicyGenerator {
    Pattern rulePattern = Pattern.compile("ruleName\\s*=\\s*(\\w*)\\s*,\\s*body\\s*=\\s*(.*);");
    private static final Logger logger = Logger.getLogger(TxtPolicyGenerator.class);
    private File file;


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Policy generatePolicy() throws Exception {
        String policyName = getFileName(file);
        Set<Rule> rules = getRules();
        return new Policy(rules, policyName);
    }

    public Set<Rule> getRules() throws Exception {
        Set<Rule> rules = new HashSet<>();
        Scanner scanner = new Scanner(getFile());
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
        logger.info(
                String.format("Building rule with name=%s and body=%s", ruleName, ruleExpression));
        RecursiveExpressionBuilder expr = new RecursiveExpressionBuilder(ruleExpression + "\r\n");
        Expression ruleExpr = expr.build();
        return new RuleByExpression(ruleExpr, ruleName);
    }
}
