package org.zaproxy.zap.extension.filetester.model;

import java.io.IOException;
import java.util.List;

public interface IDownloadedFile {
    boolean isValid() throws IOException;
    List<FileTestResult> getTestResults();
}
