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
import java.util.List;
import javax.swing.*;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.AbstractPanel;
import org.zaproxy.zap.extension.policyverifier.models.Policy;
import org.zaproxy.zap.extension.policyverifier.models.Rule;
import org.zaproxy.zap.utils.FontUtils;

public class PolicyVerifierPanel extends AbstractPanel {
    private static PolicyVerifierPanel solePanelView;
    private static final long serialVersionUID = -2934074835463140074L;
    private JPanel mainPanel;

    private JScrollPane jScrollPane = null;

    public PolicyVerifierPanel() {
        super();
        if (solePanelView != null) {
            throw new RuntimeException(
                    "Use getInstance() method to get the single instance of this class.");
        }
        initialize();
    }

    public static PolicyVerifierPanel getSingleton() {
        if (solePanelView == null) {
            solePanelView = new PolicyVerifierPanel();
        }
        return solePanelView;
    }

    /** This method initializes this */
    private void initialize() {
        this.setLayout(new BorderLayout());
        this.setName(Constant.messages.getString("policyverifier.menu.title")); // ZAP: i18n
        this.add(getMainPanel(), BorderLayout.CENTER);
        this.setShowByDefault(true);
    }

    private JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(getJScrollPane(), BorderLayout.CENTER);
        }
        return mainPanel;
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setName("jScrollPane");
            jScrollPane.setHorizontalScrollBarPolicy(
                    javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }
        return jScrollPane;
    }

    public void updateAndDisplayLoadedPolicies(List<Policy> policies) {
        JTextPane pane = new JTextPane();
        pane.setEditable(false);
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='overflow:auto;'><table>");
        for (Policy policy : policies) {
            sb.append(
                    String.format(
                            "<tr><td><b> Policy Name </b></td><td> %s </td><td><table>",
                            policy.getName()));
            sb.append("<tr><td><b> Rule Name </b></td></tr>");
            for (Rule rule : policy.getRules()) {
                sb.append(String.format("<tr><td> %s </td></tr>", rule.getName()));
            }
            sb.append("</table></td></tr></table>");
        }
        sb.append("</table></body></html>");
        // Obtain (and set) a font with the size defined in the options
        pane.setFont(FontUtils.getFont("Dialog", Font.PLAIN));
        pane.setContentType("text/html");
        pane.setText(sb.toString());
        jScrollPane.setViewportView(pane);
    }
}
