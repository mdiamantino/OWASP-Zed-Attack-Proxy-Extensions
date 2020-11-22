package org.zaproxy.zap.extension.filetester.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PngFile extends ImageFile {

    private static final int PNG_MAGIC_NUMBER = 0x89504e47;

    public PngFile(String name, InputStream file) {
        super(name, file);
    }

    @Override
    public boolean isValid() throws IOException {
        boolean isValid = false;
        FileTestResult fileInvalidation = new FileTestResult("Png Invalidation");
        if (isValid(PNG_MAGIC_NUMBER)) {
            isValid = true;
            fileInvalidation.setResult(false);
            fileInvalidation.setRemarks("");
        } else {
            fileInvalidation.setResult(true);
            fileInvalidation.setRemarks("File invalid");
        }
        this.getTestResults().add(fileInvalidation);
        return isValid;
    }
}
