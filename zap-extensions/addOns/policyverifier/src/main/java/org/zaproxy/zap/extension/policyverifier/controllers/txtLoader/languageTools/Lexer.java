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

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;

public class Lexer {
    private StreamTokenizer input;
    private OperatorEnum symbol;
    private String string;

    public Lexer(String inputString) {
        Reader reader = new StringReader(inputString);
        input = new StreamTokenizer(reader);

        input.wordChars('_', '_'); // todo
    }

    public String getString() {
        if (symbol != OperatorEnum.STRING)
            throw new RuntimeException("Cannot extract string from a non word token.");
        return string;
    }

    public OperatorEnum nextSymbol() {
        string = null;
        try {
            int token = input.nextToken();
            System.out.println("token: " + token);

            switch (token) {
                case '(':
                    symbol = OperatorEnum.LEFT;
                    break;
                case ')':
                    symbol = OperatorEnum.RIGHT;
                    break;
                case '&':
                    symbol = OperatorEnum.AND;
                    break;
                case '|':
                    symbol = OperatorEnum.OR;
                    break;
                case '!':
                    symbol = OperatorEnum.NOT;
                    break;
                case '\'':
                    symbol = OperatorEnum.STRING;
                    string = input.sval;
                    break;
                case ',':
                    symbol = OperatorEnum.COMMA;
                    break;
                case '[':
                    symbol = OperatorEnum.LEFT_BR;
                    break;
                case ']':
                    symbol = OperatorEnum.RIGHT_BR;
                    break;
                case StreamTokenizer.TT_WORD:
                    System.out.println("'" + input.sval + "'");
                    if (input.sval.equalsIgnoreCase("REQUEST_BODY")) {
                        symbol = OperatorEnum.REQUEST_BODY;
                    } else if (input.sval.equalsIgnoreCase("REQUEST_HEADER")) {
                        symbol = OperatorEnum.REQUEST_HEADER;
                    } else if (input.sval.equalsIgnoreCase("RESPONSE_BODY")) {
                        symbol = OperatorEnum.RESPONSE_BODY;
                    } else if (input.sval.equalsIgnoreCase("RESPONSE_HEADER")) {
                        symbol = OperatorEnum.RESPONSE_HEADER;
                    } else if (input.sval.equalsIgnoreCase("matchList")) {
                        symbol = OperatorEnum.MATCH_LIST;
                    } else if (input.sval.equalsIgnoreCase("matchRegex")) {
                        symbol = OperatorEnum.MATCH_REGEX;
                    }
                    break;
                case StreamTokenizer.TT_EOF:
                    symbol = OperatorEnum.EOF;
                    break;
                default:
                    symbol = OperatorEnum.INVALID;
            }
        } catch (IOException e) {
            symbol = OperatorEnum.EOF;
        }
        return symbol;
    }
}
