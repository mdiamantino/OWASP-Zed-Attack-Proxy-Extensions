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
package org.zaproxy.zap.extension.filetester.model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Report manages the generation of the report based on the latest results.
 */
public class Report {
    /**
     * Gets the list of files and their latest update on their corresponding tests.
     *
     * @param files List of files for which the report has to be generated.
     * @return List of files with the latest results.
     * @throws IOException if cannot read the input stream.
     */
    public List<IDownloadedFile> generateReport(List<IDownloadedFile> files) throws IOException {
        List<IDownloadedFile> completedFiles = new LinkedList<>();
        for (IDownloadedFile file : files) {
            List<FileTestResult> testResults = file.getTestResults();
            if (!testResults.isEmpty()) {
                completedFiles.add(file);
            }
        }
        return completedFiles;
    }
}
