package org.zaproxy.zap.extension.policyverifier.controllers.txtLoader.languageTools;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

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
        assertFalse(pred.test(msg));
        msg.setRequestBody("google, twitter");
        assertTrue(pred.test(msg));
    }

    @Test
    void build_NestedAndOrOfExpressions_TestsPass() {
        String expression = "( matchList REQUEST_BODY['google'] & matchList REQUEST_BODY['twitter'] ) | matchList REQUEST_BODY['facebook']";
        RecursiveExpressionBuilder reb = new RecursiveExpressionBuilder(expression);
        Predicate<HttpMessage> pred = reb.build();
        HttpMessage msg = new HttpMessage();
        msg.setRequestBody("google");
        assertFalse(pred.test(msg));
        msg.setRequestBody("twitter");
        assertFalse(pred.test(msg));
        msg.setRequestBody("google, twitter");
        assertTrue(pred.test(msg));
        msg.setRequestBody("facebook");
        assertTrue(pred.test(msg));
    }

    @Test
    void build_NestedOrAndOfExpressions_TestsPass() {
        String expression = "( matchList REQUEST_BODY['google'] | matchList REQUEST_BODY['twitter'] ) & matchList REQUEST_BODY['facebook']";
        RecursiveExpressionBuilder reb = new RecursiveExpressionBuilder(expression);
        Predicate<HttpMessage> pred = reb.build();
        HttpMessage msg = new HttpMessage();
        msg.setRequestBody("google, facebook");
        assertTrue(pred.test(msg));
        msg.setRequestBody("twitter, facebook");
        assertTrue(pred.test(msg));
        msg.setRequestBody("google, twitter, facebook");
        assertTrue(pred.test(msg));
        msg.setRequestBody("facebook");
        assertFalse(pred.test(msg));
        msg.setRequestBody("google, twitter");
        assertFalse(pred.test(msg));
    }

    @Test
    void build_ComplexNestedExpression1_TestsPass() {
        //   ((A | C) & ((A & D) | (A & ! D))) | (A & C) | C ->  A | C
        String A = "matchList REQUEST_BODY['google']";
        String C = "matchList REQUEST_BODY['facebook']";
        String D = "matchList REQUEST_BODY['instagram']";
        String expression = "((" + A + " | " + C + ") & ((" + A + " & " + D + ") | (" + A + " & ! " + D + "))) | (" + A + " & " + C + ") | " + C;
        RecursiveExpressionBuilder reb = new RecursiveExpressionBuilder(expression);
        Predicate<HttpMessage> pred = reb.build();
        HttpMessage msg = new HttpMessage();
        msg.setRequestBody("google, facebook");
        assertTrue(pred.test(msg));
        msg.setRequestBody("google");
        assertTrue(pred.test(msg));
        msg.setRequestBody("facebook");
        assertTrue(pred.test(msg));
        msg.setRequestBody("instagram");
        assertFalse(pred.test(msg));
    }

    @Test
    void build_ComplexNestedExpression2_TestsPass() {
        //   (!A & (A | B)) |  ((B | (A & A)) & (A | !B)) ->  A | C
        String A = "matchList REQUEST_BODY['google']";
        String B = "matchList REQUEST_BODY['facebook']";
        String expression = "(!"+A+" & ("+A+" | "+B+")) |  (("+B+" | ("+A+" & "+A+")) & ("+A+" | !"+B+"))";
        RecursiveExpressionBuilder reb = new RecursiveExpressionBuilder(expression);
        Predicate<HttpMessage> pred = reb.build();
        HttpMessage msg = new HttpMessage();
        msg.setRequestBody("google, facebook");
        assertTrue(pred.test(msg));
        msg.setRequestBody("google");
        assertTrue(pred.test(msg));
        msg.setRequestBody("facebook");
        assertTrue(pred.test(msg));
        msg.setRequestBody("instagram");
        assertFalse(pred.test(msg));
    }

    @Test
    void build_NotOfComplexExpressions_TestsPass() {
        String expression = "! ( ( matchList REQUEST_BODY['google'] | matchList REQUEST_BODY['twitter'] ) & matchList REQUEST_BODY['facebook'] )";
        RecursiveExpressionBuilder reb = new RecursiveExpressionBuilder(expression);
        Predicate<HttpMessage> pred = reb.build();
        HttpMessage msg = new HttpMessage();
        msg.setRequestBody("google, facebook");
        assertFalse(pred.test(msg));
        msg.setRequestBody("twitter, facebook");
        assertFalse(pred.test(msg));
        msg.setRequestBody("google, twitter, facebook");
        assertFalse(pred.test(msg));
        msg.setRequestBody("facebook");
        assertTrue(pred.test(msg));
        msg.setRequestBody("google, twitter");
        assertTrue(pred.test(msg));
    }

    @Test
    void build_MultipleOrOfExpressions_TestsPass() {
        StringBuilder expression = new StringBuilder();
        Set<String> toMatchSet = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            String toMatch = RandomStringUtils.random(10, true, true);
            toMatchSet.add(toMatch);
            expression.append("matchList REQUEST_BODY['").append(toMatch).append("'] |");
        }
        String toMatch = RandomStringUtils.random(10, true, true);
        toMatchSet.add(toMatch);
        expression.append("matchList REQUEST_BODY['").append(toMatch).append("']");
        RecursiveExpressionBuilder reb = new RecursiveExpressionBuilder(expression.toString());
        Predicate<HttpMessage> pred = reb.build();
        for (String toCheck : toMatchSet) {
            HttpMessage msg = new HttpMessage();
            msg.setRequestBody(toCheck);
            assertTrue(pred.test(msg));
            // Checking if anything else not accepted
            msg.setRequestBody(RandomStringUtils.random(10, true, true));
            assertFalse(pred.test(msg));
        }
    }

    @Test
    void build_MultipleAndOfExpressions_TestsPass() {
        StringBuilder expression = new StringBuilder();
        Set<String> toMatchSet = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            String toMatch = RandomStringUtils.random(10, true, true);
            toMatchSet.add(toMatch);
            expression.append("matchList REQUEST_BODY['").append(toMatch).append("'] &");
        }
        String toMatch = RandomStringUtils.random(10, true, true);
        toMatchSet.add(toMatch);
        expression.append("matchList REQUEST_BODY['").append(toMatch).append("']");
        RecursiveExpressionBuilder reb = new RecursiveExpressionBuilder(expression.toString());
        Predicate<HttpMessage> pred = reb.build();
        StringBuilder correctMatch = new StringBuilder();
        HttpMessage msg = new HttpMessage();
        for (String toCheck : toMatchSet) {
            msg.setRequestBody(toCheck);
            assertFalse(pred.test(msg));
            correctMatch.append(",").append(toCheck);
        }
        msg.setRequestBody(correctMatch.toString());
        assertTrue(pred.test(msg));
    }


    @Test
    void build_NotOfExpression_TestsPass() {
        String expression = "! matchList REQUEST_BODY['google']\r\n";
        RecursiveExpressionBuilder reb = new RecursiveExpressionBuilder(expression);
        Predicate<HttpMessage> pred = reb.build();
        HttpMessage msg = new HttpMessage();
        msg.setRequestBody("google");
        assertFalse(pred.test(msg));
    }

    @Test
    void build_DoubleNotOfExpression_TestsPass() {
        String expression = "! ! matchList REQUEST_BODY['google']\r\n";
        RecursiveExpressionBuilder reb = new RecursiveExpressionBuilder(expression);
        Predicate<HttpMessage> pred = reb.build();
        HttpMessage msg = new HttpMessage();
        msg.setRequestBody("google");
        assertTrue(pred.test(msg));
    }

    @Test
    void build_TerminalExpressionWithNoSubject_ThrowsRunTimeException() {
        String expression = "matchList ['google']\r\n";
        RecursiveExpressionBuilder reb = new RecursiveExpressionBuilder(expression);
        assertThrows(RuntimeException.class, reb::build);
    }

    @Test
    void build_TerminalExpressionWithNoArguments_ThrowsRunTimeException() {
        String expression = "matchList REQUEST_BODY[]\r\n";
        RecursiveExpressionBuilder reb = new RecursiveExpressionBuilder(expression);
        assertThrows(RuntimeException.class, reb::build);
    }


}
