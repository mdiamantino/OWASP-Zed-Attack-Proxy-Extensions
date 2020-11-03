package org.zaproxy.zap.extension.policyverifier;

import net.htmlparser.jericho.Source;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.pscan.PassiveScanThread;
import org.zaproxy.zap.extension.pscan.PluginPassiveScanner;

import java.util.Random;

public abstract class Rule extends PluginPassiveScanner {
    protected String RULE_NAME;
    protected String MESSAGE_PREFIX;
    protected int PLUGIN_ID;

    @Override
    public int getPluginId() {
        return 4830;
    }

    @Override
    public String getName() {
        return Constant.messages.getString(MESSAGE_PREFIX + "name");
    }

//    public void setPluginId() {
//        Random rand = new Random();
//        int upperbound = 1000;
//        int lowerbound = 200;
//        PLUGIN_ID = rand.nextInt(upperbound - lowerbound) + lowerbound;
//    }

    @Override
    public void setParent(PassiveScanThread parent) {
        // Nothing to do.
    }

    @Override
    public abstract void scanHttpRequestSend(HttpMessage msg, int id);

    @Override
    public abstract void scanHttpResponseReceive(HttpMessage msg, int id, Source source);

    public void notifyViolated() {
        System.out.println("violated");
    }

}