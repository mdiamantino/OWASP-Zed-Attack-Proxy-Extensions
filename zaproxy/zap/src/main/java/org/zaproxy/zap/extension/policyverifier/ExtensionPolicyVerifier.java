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

    private ZapMenuItem menuImportLoadPolicy;

    public ExtensionPolicyVerifier() {
        super(NAME);
        setI18nPrefix(PREFIX);
    }

    @Override
    public void hook(ExtensionHook extensionHook) {
        super.hook(extensionHook);

        if (getView() != null) {
            extensionHook.getHookMenu().addImportMenuItem(getMenuImportLoadPolicy());
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


    private ZapMenuItem getMenuImportLoadPolicy() {
        if (menuImportLoadPolicy == null) {
            menuImportLoadPolicy = new ZapMenuItem(PREFIX + ".topmenu.tools.title");

            menuImportLoadPolicy.addActionListener(
                    ae -> loadJarPolicy());
        }
        return menuImportLoadPolicy;
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
}
