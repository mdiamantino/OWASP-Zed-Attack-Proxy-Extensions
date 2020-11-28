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
package org.zaproxy.zap.extension.policyverifier.views;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.AbstractDialog;
import org.parosproxy.paros.view.View;

import javax.swing.*;
import java.util.Objects;

public class DocDialog extends AbstractDialog {
    private static final long serialVersionUID = 1L;

    public DocDialog() {
        super(Objects.requireNonNull(View.getSingleton()).getMainFrame(), true);
        JPanel mainPanel = new JPanel();
        setUpHelpTextBox(mainPanel);
        setUpOkButton(mainPanel);
        this.setContentPane(mainPanel);
        this.pack();
    }

    private void setUpHelpTextBox(JPanel mainPanel) {
        JLabel label = new JLabel();
        label.setText(Constant.messages.getString("policyverifier.docs.content"));
        JPanel panel = new JPanel();
        panel.add(label);
        mainPanel.add(panel);
    }

    private void setUpOkButton(JPanel mainPanel) {
        JButton btnOk = new JButton();
        btnOk.setText(Constant.messages.getString("policyverifier.docs.okbutton"));
        btnOk.addActionListener(e -> dispose());
        mainPanel.add(btnOk);
    }
}
