package org.zaproxy.zap.extension.policyverifier.rules;

public class TestUtils {
    static String buildDomainUrl(String domain) {
        return String.format("https://www.%s.com", domain);
    }

    static String buildResponseHeader(int responseLength) {
        return String.format("HTTP/1.1 200 OK\n" +
                "Content-Type: text/html; charset=utf-8\n" +
                "Server: Apache\n" +
                "Content-Length: %d\n", responseLength);
    }
}
