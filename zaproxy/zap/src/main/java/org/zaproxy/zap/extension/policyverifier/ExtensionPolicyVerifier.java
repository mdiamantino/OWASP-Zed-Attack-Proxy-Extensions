package org.zaproxy.zap.extension.policyverifier;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.zaproxy.zap.view.ZapMenuItem;

public class ExtensionPolicyVerifier extends ExtensionAdaptor {
    public static final String NAME = "ExtensionPolicyVerifier";
    protected static final String PREFIX = "policyVerifier";

    private ZapMenuItem menuImportLoadPolicy;

    private PolicyVerifierController getExtensionController() {
        return PolicyVerifierController.getSingleton();
    }

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
                    ae -> {
                        getExtensionController().loadPolicy();
                    });
        }
        return menuImportLoadPolicy;
    }

    @Override
    public String getDescription() {
        return Constant.messages.getString(PREFIX + ".desc");
    }
}
