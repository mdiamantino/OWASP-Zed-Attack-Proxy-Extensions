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

import java.awt.*;
import java.util.Objects;
import javax.swing.*;
import org.parosproxy.paros.extension.AbstractDialog;
import org.parosproxy.paros.view.View;

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

    private GridBagConstraints generatePanelForHelpTextBox() {
        GridBagConstraints gbcPanel = new GridBagConstraints();
        gbcPanel.gridx = 0;
        gbcPanel.gridy = 0;
        gbcPanel.insets = new Insets(0, 0, 0, 0);
        gbcPanel.fill = GridBagConstraints.BOTH;
        gbcPanel.anchor = GridBagConstraints.NORTHWEST;
        gbcPanel.weightx = 1.0D;
        gbcPanel.weighty = 1.0D;
        gbcPanel.ipady = 2;
        gbcPanel.gridwidth = 2;
        return gbcPanel;
    }

    private void setUpHelpTextBox(JPanel mainPanel) {
        JLabel label = new JLabel();
        label.setText(
                "<html><style>body {background-color: #FFFFFF;}pre { font-family: monospace; }<body><p>To load a new policy, you need to create a .jar file containing the rules.</p><p>ZAP and the extension are needed dependencies. Therefore, make sure to build them, and include their JARs as dependencies your policy project.</p><p>Each rule must be defined in a separate java classes, implementing the interface Rule</p><p>The rule class should implement the method <pre>boolean isValid(HttpMessage msg)</pre>, returning false when the rule is violated.</p><p>You will find a step-by-step tutorial in : <b>Group19/solutions/user_story_01/Tutorial.pdf</b></p></body></html>");
        JPanel panel = new JPanel();
        panel.add(label);
        mainPanel.add(panel, generatePanelForHelpTextBox());
    }

    private GridBagConstraints generateLayoutForOkButton() {
        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.gridx = 1;
        gbcButtons.gridy = 1;
        gbcButtons.insets = new Insets(2, 2, 2, 2);
        gbcButtons.anchor = GridBagConstraints.SOUTHEAST;
        return gbcButtons;
    }

    private void setUpOkButton(JPanel mainPanel) {
        JButton btnOk = new JButton();
        btnOk.setText("Ok");
        btnOk.addActionListener(e -> dispose());
        mainPanel.add(btnOk, generateLayoutForOkButton());
    }
}
