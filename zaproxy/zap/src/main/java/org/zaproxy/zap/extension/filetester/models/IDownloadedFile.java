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
package org.zaproxy.zap.extension.filetester.models;

import java.io.IOException;
import java.util.List;

/**
 * IDownloadedFile checks the validity of a file being downloading and gets the corresponding results.
 */
public interface IDownloadedFile {
    /**
     * Checks a file for its validity.
     *
     * @return True if the file conforms with the tests.
     * @throws IOException if cannot read the input stream.
     */
    boolean isValid() throws IOException;

    /**
     * Gets the result of the tests performed on a file.
     *
     * @return List of tests and their results that were performed on the file.
     * @throws IOException if cannot read the input stream.
     */
    List<FileTestResult> getTestResults() throws IOException;

    /**
     * Gets the name of the file.
     *
     * @return The name of the file.
     */
    String getName();

    /**
     * Checks if all the tests are completed.
     *
     * @return True if all the tests are completed.
     */
    boolean isCompleted();
}
