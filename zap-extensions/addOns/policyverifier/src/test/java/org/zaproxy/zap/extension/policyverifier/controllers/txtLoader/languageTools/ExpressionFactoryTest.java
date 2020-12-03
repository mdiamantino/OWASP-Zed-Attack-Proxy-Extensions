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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.IncompleteArgumentException;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.zaproxy.zap.extension.policyverifier.models.expressions.AbstractTerminalExpression;

class ExpressionFactoryTest {
    @Test
    void isTokenAnOperationWithCorrectToken() {
        assertTrue(ExpressionFactory.isTokenAnOperation(OperatorEnum.MATCH_LIST));
        assertTrue(ExpressionFactory.isTokenAnOperation(OperatorEnum.MATCH_REGEX));
    }

    @Test
    void isTokenAnOperationWithIncorrectToken() {
        assertFalse(ExpressionFactory.isTokenAnOperation(OperatorEnum.AND));
    }

    @Test
    void isTokenAnOperationWithNullToken() {
        assertThrows(RuntimeException.class, () -> ExpressionFactory.isTokenAnOperation(null));
    }

    @Test
    void isTokenASubjectWithCorrectToken() {
        assertTrue(ExpressionFactory.isTokenASubject(OperatorEnum.REQUEST_BODY));
        assertTrue(ExpressionFactory.isTokenASubject(OperatorEnum.REQUEST_HEADER));
        assertTrue(ExpressionFactory.isTokenASubject(OperatorEnum.RESPONSE_BODY));
        assertTrue(ExpressionFactory.isTokenASubject(OperatorEnum.RESPONSE_HEADER));
    }

    @Test
    void isTokenASubjectWithIncorrectToken() {
        assertFalse(ExpressionFactory.isTokenASubject(OperatorEnum.AND));
    }

    @Test
    void isTokenASubjectWithNullToken() {
        assertFalse(ExpressionFactory.isTokenASubject(null));
    }

    @Test
    void extractOperationFromSymbolWithCorrectArguments() {
        assertTrue(
                ExpressionFactory.extractOperationFromSymbol(
                                OperatorEnum.MATCH_REGEX,
                                OperatorEnum.REQUEST_BODY,
                                generateListOfRandomArgs(1))
                        instanceof AbstractTerminalExpression);
    }

    @Test
    void extractOperationFromSymbolWithWrongOperation() {
        assertThrows(
                RuntimeException.class,
                () ->
                        ExpressionFactory.extractOperationFromSymbol(
                                OperatorEnum.AND,
                                OperatorEnum.REQUEST_BODY,
                                generateListOfRandomArgs(1)));
    }

    @Test
    void extractOperationFromSymbolWithNullOperation() {
        assertThrows(
                RuntimeException.class,
                () ->
                        ExpressionFactory.extractOperationFromSymbol(
                                null, OperatorEnum.REQUEST_BODY, generateListOfRandomArgs(1)));
    }

    @Test
    void extractOperationFromSymbolWithWrongSubject() {
        assertThrows(
                RuntimeException.class,
                () ->
                        ExpressionFactory.extractOperationFromSymbol(
                                OperatorEnum.MATCH_REGEX,
                                OperatorEnum.AND,
                                generateListOfRandomArgs(1)));
    }

    @Test
    void extractOperationFromSymbolWithNullSubject() {
        assertThrows(
                RuntimeException.class,
                () ->
                        ExpressionFactory.extractOperationFromSymbol(
                                OperatorEnum.MATCH_REGEX, null, generateListOfRandomArgs(1)));
    }

    @Test
    void extractOperationFromSymbolWithNullList() {
        assertThrows(
                NullPointerException.class,
                () ->
                        ExpressionFactory.extractOperationFromSymbol(
                                OperatorEnum.MATCH_REGEX, OperatorEnum.REQUEST_BODY, null));
    }

    @Test
    void extractOperationFromSymbolWithEmptyList() {
        assertThrows(
                IncompleteArgumentException.class,
                () ->
                        ExpressionFactory.extractOperationFromSymbol(
                                OperatorEnum.MATCH_REGEX,
                                OperatorEnum.REQUEST_BODY,
                                new LinkedList<>()));
    }

    @Test
    void extractOperationFromSymbolWithMoreThanOneElement() {
        assertThrows(
                IncompleteArgumentException.class,
                () ->
                        ExpressionFactory.extractOperationFromSymbol(
                                OperatorEnum.MATCH_REGEX,
                                OperatorEnum.REQUEST_BODY,
                                generateListOfRandomArgs(2)));
    }

    private List<String> generateListOfRandomArgs(int nArgs) {
        List<String> args = new ArrayList<>();
        for (int i = 0; i < nArgs; i++) {
            args.add(RandomStringUtils.randomAlphabetic(5));
        }
        return args;
    }
}
