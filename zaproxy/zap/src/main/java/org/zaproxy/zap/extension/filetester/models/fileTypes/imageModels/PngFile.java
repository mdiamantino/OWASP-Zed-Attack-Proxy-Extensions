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
package org.zaproxy.zap.extension.filetester.models.fileTypes.imageModels;

import org.parosproxy.paros.Constant;
import org.zaproxy.zap.extension.filetester.models.FileTestResult;

import java.io.IOException;
import java.io.InputStream;

/**
 * PngFile manages the validation of PNG file type.
 */
public class PngFile extends ImageFile {

    private static final int PNG_MAGIC_NUMBER = 0x89504e47;
    private static final String TEST_PNG_VALIDATION = Constant.messages.getString("filetester.png.valid");

    public PngFile(String name, InputStream file) {
        super(name, file);
    }

    @Override
    public boolean isValid() throws IOException {
        boolean isValid = false;
        FileTestResult fileInvalidation = new FileTestResult(TEST_PNG_VALIDATION);
        if (isValid(PNG_MAGIC_NUMBER)) {
            isValid = true;
            fileInvalidation.setResult(false);
            fileInvalidation.setRemarks("The file passed the test.");
        } else {
            fileInvalidation.setResult(true);
            fileInvalidation.setRemarks("File invalid");
        }
        this.getTestResults().add(fileInvalidation);
        isCompleted = true;
        return isValid;
    }
}
