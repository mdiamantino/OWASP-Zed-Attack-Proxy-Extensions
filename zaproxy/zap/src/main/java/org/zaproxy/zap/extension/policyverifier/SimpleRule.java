package org.zaproxy.zap.extension.policyverifier;

import net.htmlparser.jericho.Source;
import org.parosproxy.paros.network.HttpMessage;

import java.util.Enumeration;

public class SimpleRule extends Rule {
    private final String RULE_NAME = "SimpleRule";

    @Override
    public void scanHttpRequestSend(HttpMessage msg, int id) {

    }

    @Override
    public void scanHttpResponseReceive(HttpMessage msg, int id, Source source) {
        System.out.println(msg.getResponseHeader().getHeadersAsString());
    }
}
