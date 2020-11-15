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
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.view.ZapMenuItem;

public class ExtensionFileTester extends ExtensionAdaptor {
    public static final String NAME = "ExtensionFileTester";
    protected static final String PREFIX = "filetester";
    private javax.swing.JMenu menuFileTester = null;

    public ExtensionFileTester() {
        super(NAME);
        setI18nPrefix(PREFIX);
    }

    @Override
    public void hook(ExtensionHook extensionHook) {
        super.hook(extensionHook);

        if (getView() != null) {
            extensionHook.getHookMenu().addNewMenu(getMenuFileTesterPlugin());
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
        if (menuFileTester != null) {
            menuFileTester.add(getMenuOptionHelp()); // Adding Help button
        }
    }

    private javax.swing.JMenu getMenuFileTesterPlugin() {
        if (menuFileTester == null) {
            menuFileTester = new javax.swing.JMenu();
            menuFileTester.setText(Constant.messages.getString(PREFIX + ".menu.title"));
            setUpPluginMenu();
        }
        return menuFileTester;
    }
}
