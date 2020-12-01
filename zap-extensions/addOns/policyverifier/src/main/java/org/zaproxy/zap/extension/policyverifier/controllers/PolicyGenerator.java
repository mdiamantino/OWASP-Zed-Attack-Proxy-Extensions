package org.zaproxy.zap.extension.policyverifier.controllers;

import org.apache.commons.io.FilenameUtils;
import org.zaproxy.zap.extension.policyverifier.models.Policy;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

import java.io.File;
import java.util.Set;

public interface PolicyGenerator {

    File getFile();

    void setFile(File file);

    default String getFileName(File file) {
        return FilenameUtils.removeExtension(file.getName());
    }

    Policy generatePolicy() throws Exception;

    Set<Rule> getRules() throws Exception;
}
