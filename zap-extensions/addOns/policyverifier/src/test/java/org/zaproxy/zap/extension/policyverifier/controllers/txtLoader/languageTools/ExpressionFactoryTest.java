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

class ExpressionFactoryTest {
    /*
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
               String expected =
                       "Not enough arguments were provided to match against the header. (Min 2 arguments)";
               assertTrue(e.getCause().toString().contains(expected));
           }
       }

       @Test
       void extractOperationFromSymbol_OperatorBodyWithRegex_InstantiatedSuccessfully()
               throws Exception {
           List<String> args = generateListOfRandomArgs(1);
           Expression exp = ExpressionFactory.extractOperationFromSymbol(OperatorEnum.MRQBR, args);
           assertTrue(exp instanceof RequestBodyMatchRegexExpression);
       }

       @Test
       void extractOperationFromSymbol_OperatorBodyWithRegexTwoArgs_InstantiatedSuccessfully()
               throws Exception {
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
               String expected =
                       "Not enough arguments were provided to match against the header. (Min 2 arguments)";
               assertTrue(e.getCause().toString().contains(expected));
           }
       }

       @Test
       void extractOperationFromSymbol_NoTrueOperator_ThrowsNullPointer() {
           List<String> args = generateListOfRandomArgs(2);
           assertThrows(
                   NullPointerException.class,
                   () -> ExpressionFactory.extractOperationFromSymbol(OperatorEnum.COMMA, args));
       }
    */
}
