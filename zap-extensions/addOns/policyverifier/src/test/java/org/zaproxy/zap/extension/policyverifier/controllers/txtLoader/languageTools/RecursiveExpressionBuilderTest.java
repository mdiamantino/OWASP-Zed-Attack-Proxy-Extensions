package org.zaproxy.zap.extension.policyverifier.controllers.txtLoader.languageTools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RecursiveExpressionBuilderTest {
    @Test
    void recursiveExpressionIncorrectBuild() {
        String testString = "REQUEST_BODY REQUEST_HEADER RESPONSE_BODY RESPONSE_HEADER";
        RecursiveExpressionBuilder reb = new RecursiveExpressionBuilder(testString);
        assertThrows(RuntimeException.class, () ->
                reb.build());
    }
}
