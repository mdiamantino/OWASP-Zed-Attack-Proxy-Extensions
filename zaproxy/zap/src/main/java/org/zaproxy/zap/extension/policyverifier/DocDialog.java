package org.zaproxy.zap.extension.policyverifier;

import org.parosproxy.paros.extension.AbstractDialog;

import javax.swing.*;
import java.awt.*;

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
        return "<html><style>\n" +
                "body {\n" +
                "  background-color: #FFFFFF;\n" +
                "}<body><p>TestTestTestTestTestTestTestTest</p></body></html>";
    }
}
