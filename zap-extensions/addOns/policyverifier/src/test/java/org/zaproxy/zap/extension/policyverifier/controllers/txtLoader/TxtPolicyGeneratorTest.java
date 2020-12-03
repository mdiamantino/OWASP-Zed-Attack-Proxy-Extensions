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

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

class TxtPolicyGeneratorTest {
    private TxtPolicyGenerator generator;
    private String TEST_RESOURCES_PATH =
            System.getProperty("user.dir")
                    + "/src/test/resources/org/zaproxy/zap/extension/policyverifier/txtFiles";

    @BeforeEach
    void setup() {
        generator = new TxtPolicyGenerator();
    }

    @Test
    void getFile_emptyWhenInitialized_good() {
        assertNull(generator.getFile());
    }

    @Test
    void setFile_GoodPolicy_GotEqualsOriginal() {
        File txtFile = new File(TEST_RESOURCES_PATH + "/GoodExpression.txt");
        generator.setFile(txtFile);
        assertEquals(txtFile, generator.getFile());
    }

    @Test
    void setFile_EmptyFile_Throws() {
        File txtFile = new File(TEST_RESOURCES_PATH + "/emptyPolicy.txt");
        assertThrows(IllegalArgumentException.class, () -> generator.setFile(txtFile));
    }

    @Test
    void generatePolicy() {}

    @Test
    void getRules_NoRulesNames_Throws() {
        File emptyFile = new File(TEST_RESOURCES_PATH + "/PolicyWrongSyntax1_NoRuleName.txt");
        generator.setFile(emptyFile);
        assertThrows(IllegalArgumentException.class, () -> generator.getRules());
    }

    @Test
    void getRules_NoSemicolon_Throws() {
        File noSemicolon = new File(TEST_RESOURCES_PATH + "/PolicyWrongSyntax1_NoSemicolon.txt");
        generator.setFile(noSemicolon);
        assertThrows(IllegalArgumentException.class, () -> generator.getRules());
    }

    @Test
    void getRules_NoRuleBody_Throws() {
        File noRuleBodyFile = new File(TEST_RESOURCES_PATH + "/PolicyWrongSyntax1_NoRuleBody.txt");
        generator.setFile(noRuleBodyFile);
        assertThrows(IllegalArgumentException.class, () -> generator.getRules());
    }

    @Test
    void getRules_OneRulePolicy_CorrectSet() throws Exception {
        File goodExpression = new File(TEST_RESOURCES_PATH + "/GoodExpression.txt");
        generator.setFile(goodExpression);
        Set<Rule> rulesSet = generator.getRules();
        assertEquals(1, rulesSet.size());
        List<Rule> toList = new ArrayList<>(rulesSet);
        assertEquals("MustContainGoogle", toList.get(0).getName());
    }

    @Test
    void getRules_MultipleRulesPolicy_CorrectSet() throws Exception {
        File goodExpression = new File(TEST_RESOURCES_PATH + "/MultipleRulesPolicy.txt");
        generator.setFile(goodExpression);
        Set<Rule> rulesSet = generator.getRules();
        assertEquals(3, rulesSet.size());
    }
}
