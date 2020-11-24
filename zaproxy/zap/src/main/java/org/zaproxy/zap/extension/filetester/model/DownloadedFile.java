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
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public abstract class DownloadedFile implements IDownloadedFile {

    private final String name;
    private final InputStream file;
    private final List<FileTestResult> testResults;
    protected boolean isCompleted;

    public DownloadedFile(String name, InputStream file) {
        this.file = file;
        this.name = name;
        testResults = new LinkedList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<FileTestResult> getTestResults() throws IOException {
        return testResults;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Gets the input file stream.
     *
     * @return The input stream of the file.
     */
    public InputStream getFile() {
        return file;
    }
}
