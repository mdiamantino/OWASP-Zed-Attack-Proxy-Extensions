package org.zaproxy.zap.extension.policyverifier;

import java.io.File;
import java.util.List;

public class PolicyVerifierModel {
    private static PolicyVerifierModel soleModel;
    private List<Policy> loadedPolicies = null;

    private PolicyVerifierModel(){
        if (soleModel != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static PolicyVerifierModel getSingleton(){
        if (soleModel == null){
            soleModel = new PolicyVerifierModel();
        }
        return soleModel;
    }


    public void addPolicy(File file) {

    }
}
