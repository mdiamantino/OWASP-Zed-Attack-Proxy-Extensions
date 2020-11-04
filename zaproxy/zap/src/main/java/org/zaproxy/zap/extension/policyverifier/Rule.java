package org.zaproxy.zap.extension.policyverifier;

import org.parosproxy.paros.network.HttpMessage;

public interface Rule {
    boolean isValid(HttpMessage msg);

    String getName();
}