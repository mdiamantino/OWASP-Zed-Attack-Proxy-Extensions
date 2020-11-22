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
package org.zaproxy.zap.extension.filetester;

import java.util.Objects;

import org.apache.commons.httpclient.URI;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpSender;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.network.HttpSenderListener;
import org.zaproxy.zap.view.ZapMenuItem;
import org.apache.log4j.Logger;
import org.parosproxy.paros.extension.ViewDelegate;
import org.parosproxy.paros.core.proxy.ProxyListener;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;

public class ExtensionFileTester extends ExtensionAdaptor implements HttpSenderListener {
    public static final String NAME = "ExtensionFileTester";
    protected static final String PREFIX = "filetester";
    private javax.swing.JMenu menuPolicyPlugin = null;
    private int enable_step = 0;
    private static Logger log = Logger.getLogger(ExtensionFileTester.class);

    public ExtensionFileTester() {
        super(NAME);
        setI18nPrefix(PREFIX);
    }

    @Override
    public void hook(ExtensionHook extensionHook) {
        super.hook(extensionHook);

        extensionHook.addHttpSenderListener(this);
        if (getView() != null) {
            extensionHook.getHookMenu().addNewMenu(getMenuPolicyPlugin());
        }
    }

    @Override
    public boolean canUnload() {
        return true;
    }

    @Override
    public void unload() {
        super.unload();
    }

    @Override
    public String getDescription() {
        return Constant.messages.getString(PREFIX + ".desc");
    }

    /**
     * View method used to retrieve the loaded file from the view through a graphical file chooser.
     * Only JAR files are accepted. When a valid file is picked, it is passed to the controller
     * (PolicyLoaderController).
     */


    private ZapMenuItem getMenuOptionLoadPolicy() {
        ZapMenuItem menuLoadPolicy = new ZapMenuItem("Activate/Deactivate Extension");
        menuLoadPolicy.addActionListener(e -> {
            this.enable_step += 1;
            if(this.enable_step == 1) {
                log.info("FileTester is now enabled");
                log.info("Value is: " + this.enable_step);
            }
            else if(this.enable_step == 2){
                log.info("Filetester is now disabled.");
                log.info("Value is: " + this.enable_step);
                enable_step =0;
            }
        });
        return menuLoadPolicy;
    }

    private ZapMenuItem getMenuOptionHelp() {
        ZapMenuItem menuHelp = new ZapMenuItem("Help (Documentation)");
        menuHelp.addActionListener(
                e -> {
                    DocDialog dialog =
                            new DocDialog(
                                    Objects.requireNonNull(View.getSingleton()).getMainFrame(),
                                    true);
                    dialog.setVisible(true);
                });
        return menuHelp;
    }

    private ZapMenuItem getReportOption() {
        ZapMenuItem menuReport = new ZapMenuItem("Get Report of Scans");
        menuReport.addActionListener(
                e -> {
                    DocDialog dialog =
                            new DocDialog(
                                    Objects.requireNonNull(View.getSingleton()).getMainFrame(),
                                    true);
                    dialog.setVisible(true);
                });
        return menuReport;
    }

    private void setUpPluginMenu() {
        if (menuPolicyPlugin != null) {
            menuPolicyPlugin.add(getMenuOptionLoadPolicy()); // Adding loading button
            menuPolicyPlugin.add(getMenuOptionHelp()); // Adding Help button
            menuPolicyPlugin.add(getReportOption()); //Adding the report button
        }
    }

    private javax.swing.JMenu getMenuPolicyPlugin() {
        if (menuPolicyPlugin == null) {
            menuPolicyPlugin = new javax.swing.JMenu();
            menuPolicyPlugin.setText("Extension FileTester");
            setUpPluginMenu();
        }
        return menuPolicyPlugin;
    }

    @Override
    public int getListenerOrder() {
        return 1;
    }

    @Override
    public void onHttpRequestSend(HttpMessage msg, int initiator, HttpSender sender) {
        scan(msg);
    }

    @Override
    public void onHttpResponseReceive(HttpMessage msg, int initiator, HttpSender sender) {
        scan(msg);
    }

    private boolean scan(HttpMessage msg) {
        if (this.enable_step  == 2) { // The extension is disabled so we do not react
        return true;
        }
        String site = msg.getRequestHeader().getHostName() + ":" + msg.getRequestHeader().getHostPort();
        log.info(site);
        return true;
    }

}
