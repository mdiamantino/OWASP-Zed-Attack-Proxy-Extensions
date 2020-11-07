package org.zaproxy.zap.extension.policyverifier;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.policyverifier.controllers.PolicyLoaderController;
import org.zaproxy.zap.view.ZapMenuItem;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.Objects;

public class ExtensionPolicyVerifier extends ExtensionAdaptor {
    public static final String NAME = "ExtensionPolicyVerifier";
    protected static final String PREFIX = "policyVerifier";
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
     * View method used to retrieve the loaded file from the view through a filechooser only accepting JAR files,
     * AND communicate to the controller of the extension by passing the policy file
     */
    public void loadJarPolicy() {
        JFileChooser fileChooser = new JFileChooser(Constant.getContextsDir());
        fileChooser.setAcceptAllFileFilterUsed(false);  // All types of file are not accept
        FileNameExtensionFilter jarFilter = new FileNameExtensionFilter("Jar files", "jar"); // Accepting .jar only
        fileChooser.setFileFilter(jarFilter);

        File file;
        int rc = fileChooser.showOpenDialog(Objects.requireNonNull(View.getSingleton()).getMainFrame());
        file = fileChooser.getSelectedFile();
        if (rc == JFileChooser.APPROVE_OPTION) {
            try {
                file = fileChooser.getSelectedFile();
                if (file == null || !file.exists()) {
                    View.getSingleton().showWarningDialog("The file is empty or does not exists");
                } else {
                    PolicyLoaderController.getSingleton().loadPolicy(file);
                }
            } catch (Exception ex) {
                View.getSingleton().showWarningDialog("There was an error while loading the jar");
            }

        }
    }

    private ZapMenuItem getMenuOptionLoadPolicy() {
        ZapMenuItem menuLoadPolicy = new ZapMenuItem("Load New Policy");
        menuLoadPolicy.addActionListener(
                ae -> loadJarPolicy());
        return menuLoadPolicy;
    }

    private ZapMenuItem getMenuOptionHelp() {
        ZapMenuItem menuHelp = new ZapMenuItem("Help");
        menuHelp.addActionListener(
                e -> {
                    DocDialog dialog = new DocDialog(View.getSingleton().getMainFrame(), true);
                    dialog.setVisible(true);
                });
        return menuHelp;
    }

    private void setUpPluginMenu() {
        if (menuPolicyPlugin != null) {
            menuPolicyPlugin.add(getMenuOptionLoadPolicy());    // Adding loading button
            menuPolicyPlugin.add(getMenuOptionHelp());          // Adding Help button
        }
    }

    private javax.swing.JMenu getMenuPolicyPlugin() {
        if (menuPolicyPlugin == null) {
            menuPolicyPlugin = new javax.swing.JMenu();
            menuPolicyPlugin.setText("Policy Checker");
            setUpPluginMenu();
        }
        return menuPolicyPlugin;
    }
}
