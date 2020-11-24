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

import java.awt.*;
import javax.swing.*;
import org.parosproxy.paros.extension.AbstractDialog;

public class DocDialog extends AbstractDialog {
    private static final long serialVersionUID = 1L;

    public DocDialog(Frame owner, boolean modal) {
        super(owner, modal);
        JPanel mainPanel = new JPanel();
        setUpHelpTextBox(mainPanel);
        setUpOkButton(mainPanel);
        this.setContentPane(mainPanel);
        this.pack();
    }

    private void setUpHelpTextBox(JPanel mainPanel) {
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
        JPanel panel = new JPanel();
        JLabel label = new JLabel();
        label.setText(getHelpDoc());
        panel.add(label);
        mainPanel.add(panel, gbcPanel);
    }

    private void setUpOkButton(JPanel mainPanel) {
        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.gridx = 1;
        gbcButtons.gridy = 1;
        gbcButtons.insets = new Insets(2, 2, 2, 2);
        gbcButtons.anchor = GridBagConstraints.SOUTHEAST;
        JButton btnOk = new JButton();
        btnOk.setText("Ok");
        btnOk.addActionListener(e -> dispose());
        mainPanel.add(btnOk, gbcButtons);
    }

    private String getHelpDoc() {
        return "<html><style>\n"
                + "body {\n"
                + "  background-color: #FFFFFF;\n"
                + "}\n"
                + "pre { font-family: monospace; }\n"
                + "<body><p>Click on the 'Activate Extension' button to start scanning the files that are being downloaded  .</p>"
                + "<p>You will find a more detailed documentation in : <b>Group19/solutions/user_story_07/Extension Documentation.pdf</b></p>"
                + "</body></html>";
    }
}
