package org.zaproxy.zap.extension.filetester.model;

import java.io.File;
import java.io.IOException;

public class PngFile extends ImageFile {

    private static final int PNG_MAGIC_NUMBER = 0x89504e47;

    public PngFile(File file) {
        super(file);
    }

    @Override
    public boolean isValid() throws IOException {
        boolean isValid = false;
        FileTestResult fileInvalidation = new FileTestResult("Png Invalidation");
        if (isValid(PNG_MAGIC_NUMBER)) {
            isValid = true;
            fileInvalidation.setResult(false);
        } else {
            fileInvalidation.setResult(true);
        }
        this.getTestResults().add(fileInvalidation);
        return isValid;
    }
}
