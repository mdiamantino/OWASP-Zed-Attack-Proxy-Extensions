package org.zaproxy.zap.extension.policyverifier.controllers.txtLoader.languageTools;

import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RecursiveExpressionBuilderTest {
    @Test
    void recursiveExpressionIncorrectBuild() {
        String testString = "REQUEST_BODY REQUEST_HEADER RESPONSE_BODY RESPONSE_HEADER";
        RecursiveExpressionBuilder reb = new RecursiveExpressionBuilder(testString);
        assertThrows(RuntimeException.class, reb::build);
    }

    @Test
    void build_OrOfExpressions_TestsPass() {
        String expression = "matchList REQUEST_BODY['google'] | matchList REQUEST_BODY['twitter']";
        RecursiveExpressionBuilder reb = new RecursiveExpressionBuilder(expression);
        Predicate<HttpMessage> pred = reb.build();
        HttpMessage msg = new HttpMessage();
        msg.setRequestBody("google");
        assertTrue(pred.test(msg));
        msg.setRequestBody("twitter");
        assertTrue(pred.test(msg));
        msg.setRequestBody("google, twitter");
        assertTrue(pred.test(msg));
    }

    @Test
    void build_AndOfExpressions_TestsPass() {
        String expression = "matchList REQUEST_BODY['google'] & matchList REQUEST_BODY['twitter']";
        RecursiveExpressionBuilder reb = new RecursiveExpressionBuilder(expression);
        Predicate<HttpMessage> pred = reb.build();
        HttpMessage msg = new HttpMessage();
        msg.setRequestBody("google");
        assertFalse(pred.test(msg));
        msg.setRequestBody("twitter");
        assertTrue(pred.test(msg));
        msg.setRequestBody("google, twitter");
        assertTrue(pred.test(msg));
    }

}
