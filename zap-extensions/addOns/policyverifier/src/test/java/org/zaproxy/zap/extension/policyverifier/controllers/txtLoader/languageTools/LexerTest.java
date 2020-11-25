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
    void getString_nonStringOperator_thorws() {
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
    void nextSymbol_Brakets_MapsCorrectly() {
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
    void nextSymbol_OperationMappedCorrectly_MapsCorrectly() {
        String testString = "MRQHL MRQHR MRSHL MRSHR MRQBR MRSBR";
        Lexer lexer = new Lexer(testString);
        assertEquals(OperatorEnum.MRQHL, lexer.nextSymbol());
        assertEquals(OperatorEnum.MRQHR, lexer.nextSymbol());
        assertEquals(OperatorEnum.MRSHL, lexer.nextSymbol());
        assertEquals(OperatorEnum.MRSHR, lexer.nextSymbol());
        assertEquals(OperatorEnum.MRQBR, lexer.nextSymbol());
        assertEquals(OperatorEnum.MRSBR, lexer.nextSymbol());
    }
}