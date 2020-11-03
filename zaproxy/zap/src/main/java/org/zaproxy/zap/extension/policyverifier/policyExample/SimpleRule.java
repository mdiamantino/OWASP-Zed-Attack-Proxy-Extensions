package org.zaproxy.zap.extension.policyverifier.policyExample;

import net.htmlparser.jericho.Source;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.Rule;


public class SimpleRule extends Rule {

    public SimpleRule(){
        RULE_NAME = "SimpleRule";
    }

    @Override
    public void scanHttpRequestSend(HttpMessage msg, int id) {
        // NOTHING TO DO
    }

    @Override
    public void scanHttpResponseReceive(HttpMessage msg, int id, Source source) {
        System.out.println(msg.getResponseHeader().getHeadersAsString());
    }
}
