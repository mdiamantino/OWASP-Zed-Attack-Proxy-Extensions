package org.zaproxy.zap.extension.policyverifier.controllers.txtLoader.languageTools;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.zaproxy.zap.extension.policyverifier.models.expressions.Expression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.RequestBodyMatchRegexExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.RequestHeaderMatchListExpression;
import org.zaproxy.zap.extension.policyverifier.models.expressions.terminal.concrete.RequestHeaderMatchRegexExpression;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionFactoryTest {

    @Test
    void isTokenAnOperation_EveryOperation_TrueInMap() {
        assertTrue(ExpressionFactory.isTokenAnOperation(OperatorEnum.MRQHL));
        assertTrue(ExpressionFactory.isTokenAnOperation(OperatorEnum.MRQHR));
        assertTrue(ExpressionFactory.isTokenAnOperation(OperatorEnum.MRSHL));
        assertTrue(ExpressionFactory.isTokenAnOperation(OperatorEnum.MRSHR));
        assertTrue(ExpressionFactory.isTokenAnOperation(OperatorEnum.MRQBR));
        assertTrue(ExpressionFactory.isTokenAnOperation(OperatorEnum.MRSBR));
    }

    @Test
    void isTokenAnOperation_SeparationTokenCharacters_FalseNotInMap() {
        assertFalse(ExpressionFactory.isTokenAnOperation(OperatorEnum.LEFT));
        assertFalse(ExpressionFactory.isTokenAnOperation(OperatorEnum.RIGHT));
        assertFalse(ExpressionFactory.isTokenAnOperation(OperatorEnum.AND));
        assertFalse(ExpressionFactory.isTokenAnOperation(OperatorEnum.OR));
        assertFalse(ExpressionFactory.isTokenAnOperation(OperatorEnum.NOT));
        assertFalse(ExpressionFactory.isTokenAnOperation(OperatorEnum.LEFT_BR));
        assertFalse(ExpressionFactory.isTokenAnOperation(OperatorEnum.RIGHT_BR));
        assertFalse(ExpressionFactory.isTokenAnOperation(OperatorEnum.EOF));
        assertFalse(ExpressionFactory.isTokenAnOperation(OperatorEnum.INVALID));
    }

    private List<String> generateListOfRandomArgs(int nArgs) {
        List<String> args = new ArrayList<>();
        for (int i = 0; i < nArgs; i++) {
            args.add(RandomStringUtils.randomAlphabetic(5));
        }
        return args;
    }

    @Test
    void extractOperationFromSymbol_OperatorWithList_InstantiatedSuccessfully() throws Exception {
        List<String> args = generateListOfRandomArgs(2);
        Expression exp = ExpressionFactory.extractOperationFromSymbol(OperatorEnum.MRQHL, args);
        assertTrue(exp instanceof RequestHeaderMatchListExpression);
    }

    @Test
    void extractOperationFromSymbol_OperatorWithListAndWrongArgs_Throws() {
        List<String> args = generateListOfRandomArgs(1);
        try {
            ExpressionFactory.extractOperationFromSymbol(OperatorEnum.MRQHL, args);
        } catch (Exception e) {
            String expected = "Not enough arguments were provided to match against the header. (Min 2 arguments)";
            assertTrue(e.getCause().toString().contains(expected));
        }
    }

    @Test
    void extractOperationFromSymbol_OperatorBodyWithRegex_InstantiatedSuccessfully() throws Exception {
        List<String> args = generateListOfRandomArgs(1);
        Expression exp = ExpressionFactory.extractOperationFromSymbol(OperatorEnum.MRQBR, args);
        assertTrue(exp instanceof RequestBodyMatchRegexExpression);
    }

    @Test
    void extractOperationFromSymbol_OperatorBodyWithRegexTwoArgs_InstantiatedSuccessfully() throws Exception {
        List<String> args = generateListOfRandomArgs(2);
        Expression exp = ExpressionFactory.extractOperationFromSymbol(OperatorEnum.MRQBR, args);
        assertTrue(exp instanceof RequestBodyMatchRegexExpression);
    }

    @Test
    void extractOperationFromSymbol_OperatorWithListAndWrongNArgs_Throws() {
        List<String> args = generateListOfRandomArgs(1);
        try {
            ExpressionFactory.extractOperationFromSymbol(OperatorEnum.MRQBR, args);
        } catch (Exception e) {
            String expected = "Not enough arguments were provided to match against the header. (Min 2 arguments)";
            assertTrue(e.getCause().toString().contains(expected));
        }
    }

    @Test
    void extractOperationFromSymbol_NoTrueOperator_ThrowsNullPointer() {
        List<String> args = generateListOfRandomArgs(2);
        assertThrows(NullPointerException.class, () -> ExpressionFactory.extractOperationFromSymbol(OperatorEnum.COMMA, args));

    }

}