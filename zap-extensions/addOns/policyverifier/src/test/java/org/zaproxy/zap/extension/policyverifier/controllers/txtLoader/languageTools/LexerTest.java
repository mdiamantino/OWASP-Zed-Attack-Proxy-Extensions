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

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LexerTest {
    String randomString;

    @BeforeEach
    public void setup() {
        randomString = RandomStringUtils.randomAlphabetic(5);
    }

    @Test
    void getString_nonStringOperator_throws() {
        Lexer lexer = new Lexer(randomString);
        assertThrows(RuntimeException.class, lexer::getString);
    }

    @Test
    void getString_CorrectWord_retrieveSuccessfully() {
        String word = "'" + randomString + "'";
        Lexer lexer = new Lexer(word);
        lexer.nextSymbol();
        assertEquals(randomString, lexer.getString());
    }

    @Test
    void getString_WordContainingOtherLexerCharacters_retrieveSuccessfully() {
        String modifiedRandom = randomString + ")[,";
        String word = "'" + modifiedRandom + "'";
        Lexer lexer = new Lexer(word);
        lexer.nextSymbol();
        assertEquals(modifiedRandom, lexer.getString());
    }

    @Test
    void nextSymbol_Parentheses_MapsCorrectly() {
        String testString = "()";
        Lexer lexer = new Lexer(testString);
        assertEquals(OperatorEnum.LEFT, lexer.nextSymbol());
        assertEquals(OperatorEnum.RIGHT, lexer.nextSymbol());
    }

    @Test
    void nextSymbol_Brackets_MapsCorrectly() {
        String testString = "[]";
        Lexer lexer = new Lexer(testString);
        assertEquals(OperatorEnum.LEFT_BR, lexer.nextSymbol());
        assertEquals(OperatorEnum.RIGHT_BR, lexer.nextSymbol());
    }

    @Test
    void nextSymbol_OperatorsAndOrNot_MapsCorrectly() {
        String testString = "& | !";
        Lexer lexer = new Lexer(testString);
        assertEquals(OperatorEnum.AND, lexer.nextSymbol());
        assertEquals(OperatorEnum.OR, lexer.nextSymbol());
        assertEquals(OperatorEnum.NOT, lexer.nextSymbol());
    }

    @Test
    void nextSymbol_CharactersForArgumentMapping_MapsCorrectly() {
        String testString = "' ',";
        Lexer lexer = new Lexer(testString);
        assertEquals(OperatorEnum.STRING, lexer.nextSymbol());
        assertEquals(OperatorEnum.COMMA, lexer.nextSymbol());
    }

    @Test
    void nextSymbol_NullStringMappedToEndOfFile_MapsCorrectly() {
        String testString = "";
        Lexer lexer = new Lexer(testString);
        assertEquals(OperatorEnum.EOF, lexer.nextSymbol());
    }

    @Test
    void nextSymbol_EndOfFile_MapsCorrectly() {
        String testString = "s\r\n";
        Lexer lexer = new Lexer(testString);
        lexer.nextSymbol();
        assertEquals(OperatorEnum.EOF, lexer.nextSymbol());
    }

    @Test
    void nextSymbol_StandardOperations_MapsCorrectly() {
        String testString = "REQUEST_BODY REQUEST_HEADER RESPONSE_BODY RESPONSE_HEADER matchList matchRegex";
        Lexer lexer = new Lexer(testString);
        assertEquals(OperatorEnum.REQUEST_BODY, lexer.nextSymbol());
        assertEquals(OperatorEnum.REQUEST_HEADER, lexer.nextSymbol());
        assertEquals(OperatorEnum.RESPONSE_BODY, lexer.nextSymbol());
        assertEquals(OperatorEnum.RESPONSE_HEADER, lexer.nextSymbol());
        assertEquals(OperatorEnum.MATCH_LIST, lexer.nextSymbol());
        assertEquals(OperatorEnum.MATCH_REGEX, lexer.nextSymbol());
    }
}
