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
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.policyverifier.controllers.PolicyLoaderController;
import org.zaproxy.zap.view.ZapMenuItem;

public class ExtensionPolicyVerifier extends ExtensionAdaptor {
    public static final String NAME = "ExtensionPolicyVerifier";
    protected static final String PREFIX = "policyverifier";
    private javax.swing.JMenu menuPolicyPlugin = null;

    public ExtensionPolicyVerifier() {
        super(NAME);
        setI18nPrefix(PREFIX);
    }

    @Override
    public void hook(ExtensionHook extensionHook) {
        super.hook(extensionHook);

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
    public void loadFile(String description, String extensions) {
        JFileChooser fileChooser = new JFileChooser(Constant.getContextsDir());
        fileChooser.setAcceptAllFileFilterUsed(false); // Only .jar files can be picked
        FileNameExtensionFilter jarFilter =
                new FileNameExtensionFilter(description, extensions); // Accepting .jar only
        fileChooser.setFileFilter(jarFilter);

        File file;
        int rc =
                fileChooser.showOpenDialog(
                        Objects.requireNonNull(View.getSingleton()).getMainFrame());
        if (rc == JFileChooser.APPROVE_OPTION) {
            try {
                file = fileChooser.getSelectedFile();
                if (file == null || !file.exists()) {
                    View.getSingleton()
                            .showWarningDialog(
                                    Constant.messages.getString(
                                            PREFIX + ".loader.notfoundorempty"));
                } else {
                    PolicyLoaderController.getSingleton().loadPolicy(file);
                }
            } catch (Exception ex) {
                View.getSingleton()
                        .showWarningDialog(
                                Constant.messages.getString(PREFIX + ".loader.genericerror"));
            }
        }
    }

    private ZapMenuItem getMenuOptionLoadPolicyFromJar() {
        ZapMenuItem menuLoadPolicy = new ZapMenuItem(PREFIX + ".menu.submenu.jarloader");
        menuLoadPolicy.addActionListener(ae -> loadFile("Jar files", "jar"));
        return menuLoadPolicy;
    }

    private ZapMenuItem getMenuOptionLoadPolicyFromTxt() {
        ZapMenuItem menuLoadPolicy = new ZapMenuItem(PREFIX + ".menu.submenu.txtloader");
        menuLoadPolicy.addActionListener(ae -> loadFile("Txt files", "txt"));
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
