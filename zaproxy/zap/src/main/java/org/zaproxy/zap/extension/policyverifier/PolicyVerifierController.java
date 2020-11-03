package org.zaproxy.zap.extension.policyverifier;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.policyverifier.utils.FileTypeFilter;
import org.zaproxy.zap.model.IllegalContextNameException;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Objects;

public class PolicyVerifierController {
    private static PolicyVerifierController soleController;

    private PolicyVerifierController(){
        if (soleController != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static PolicyVerifierController getSingleton(){
        if (soleController == null){
            soleController = new PolicyVerifierController();
        }
        return soleController;
    }

    public void loadPolicy(){
        JFileChooser fileChooser = new JFileChooser(Constant.getContextsDir());
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileFilter jarFilter = new FileTypeFilter(".jar", "JAR files");
        fileChooser.setFileFilter(jarFilter);

        File file;
        int rc = fileChooser.showOpenDialog(Objects.requireNonNull(View.getSingleton()).getMainFrame());
        if (rc == JFileChooser.APPROVE_OPTION) {
            try {
                file = fileChooser.getSelectedFile();
                if (file == null || !file.exists()) {
                    return;
                }
                System.out.println("loaded");
                // Import the context

                 PolicyVerifierModel.getSingleton().addPolicy(file);

                // Show the dialog
//                View.getSingleton()
//                        .showSessionDialog(
//                                Model.getSingleton().getSession(),
//                                Constant.messages.getString("context.list"),
//                                true);

            } catch (IllegalContextNameException e) {
                String detailError;
                if (e.getReason() == IllegalContextNameException.Reason.EMPTY_NAME) {
                    detailError = Constant.messages.getString("context.error.name.empty");
                } else if (e.getReason() == IllegalContextNameException.Reason.DUPLICATED_NAME) {
                    detailError = Constant.messages.getString("context.error.name.duplicated");
                } else {
                    detailError = Constant.messages.getString("context.error.name.unknown");
                }
                View.getSingleton()
                        .showWarningDialog(
                                Constant.messages.getString("context.import.error", detailError));
            } catch (Exception e1) {
                View.getSingleton()
                        .showWarningDialog(
                                Constant.messages.getString(
                                        "context.import.error", e1.getMessage()));
            }
        }
    }




}
