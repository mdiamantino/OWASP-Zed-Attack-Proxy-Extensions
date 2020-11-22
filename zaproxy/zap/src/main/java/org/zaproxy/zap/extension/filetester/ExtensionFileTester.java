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

public class ExtensionFileTester extends ExtensionAdaptor implements HttpSenderListener {
    public static final String NAME = "ExtensionFileTester";
    protected static final String PREFIX = "filetester";
    private javax.swing.JMenu menuPolicyPlugin = null;

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
        ZapMenuItem menuLoadPolicy = new ZapMenuItem(PREFIX + ".menu.submenu.loader");
//        menuLoadPolicy.addActionListener(ae -> loadJarPolicy());
        return menuLoadPolicy;
    }

    private ZapMenuItem getMenuOptionHelp() {
        ZapMenuItem menuHelp = new ZapMenuItem(PREFIX + ".menu.submenu.help");
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

    private void setUpPluginMenu() {
        if (menuPolicyPlugin != null) {
            menuPolicyPlugin.add(getMenuOptionLoadPolicy()); // Adding loading button
            menuPolicyPlugin.add(getMenuOptionHelp()); // Adding Help button
        }
    }

    private javax.swing.JMenu getMenuPolicyPlugin() {
        if (menuPolicyPlugin == null) {
            menuPolicyPlugin = new javax.swing.JMenu();
            menuPolicyPlugin.setText(Constant.messages.getString(PREFIX + ".menu.title"));
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

    private void scan(HttpMessage msg) {
        String site = msg.getRequestHeader().getHostName() + ":" + msg.getRequestHeader().getHostPort();
        System.out.println(site);
    }

}
