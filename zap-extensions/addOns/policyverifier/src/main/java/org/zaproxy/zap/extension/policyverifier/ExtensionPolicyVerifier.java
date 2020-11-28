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
package org.zaproxy.zap.extension.policyverifier;

import java.io.File;
import java.util.Objects;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.policyverifier.controllers.PolicyLoaderController;
import org.zaproxy.zap.extension.policyverifier.views.DocDialog;
import org.zaproxy.zap.extension.policyverifier.views.PolicyVerifierPanel;
import org.zaproxy.zap.view.ZapMenuItem;

public class ExtensionPolicyVerifier extends ExtensionAdaptor {
    private PolicyLoaderController policyLoaderController;
    private static final String NAME = "ExtensionPolicyVerifier";
    private final String PREFIX = "policyverifier";
    private javax.swing.JMenu menuPolicyPlugin = null;
    private DocDialog documentationDialog = new DocDialog();

    public ExtensionPolicyVerifier() {
        super(NAME);
        setI18nPrefix(PREFIX);
        policyLoaderController = new PolicyLoaderController();
    }

    @Override
    public void hook(ExtensionHook extensionHook) {
        super.hook(extensionHook);

        if (getView() != null) {
            extensionHook.getHookMenu().addNewMenu(getMenuPolicyPlugin());
            extensionHook.getHookView().addStatusPanel(PolicyVerifierPanel.getSingleton());
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

    private ZapMenuItem getMenuOptionLoadPolicyFromJar() {
        ZapMenuItem menuLoadPolicy = new ZapMenuItem(PREFIX + ".menu.submenu.jarloader");
        menuLoadPolicy.addActionListener(ae -> policyLoaderController.loadFile("Jar files", "jar"));
        return menuLoadPolicy;
    }

    private ZapMenuItem getMenuOptionLoadPolicyFromTxt() {
        ZapMenuItem menuLoadPolicy = new ZapMenuItem(PREFIX + ".menu.submenu.txtloader");
        menuLoadPolicy.addActionListener(ae -> policyLoaderController.loadFile("Txt files", "txt"));
        return menuLoadPolicy;
    }

    private ZapMenuItem getMenuOptionHelp() {
        ZapMenuItem menuHelp = new ZapMenuItem(PREFIX + ".menu.submenu.help");
        menuHelp.addActionListener(
                e -> {
                    documentationDialog.setVisible(true);
                });
        return menuHelp;
    }

    private void setUpPluginMenu() {
        if (menuPolicyPlugin != null) {
            menuPolicyPlugin.add(getMenuOptionLoadPolicyFromJar()); // Adding loading button
            menuPolicyPlugin.add(getMenuOptionLoadPolicyFromTxt());
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
}
