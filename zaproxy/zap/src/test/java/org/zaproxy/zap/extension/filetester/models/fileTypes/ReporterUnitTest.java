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
package org.zaproxy.zap.extension.filetester.models.fileTypes;

import org.junit.jupiter.api.Test;
import org.zaproxy.zap.extension.filetester.models.IDownloadedFile;
import org.zaproxy.zap.extension.filetester.models.Reporter;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReporterUnitTest {
    @Test
    public void reportWhenNoUncompletedFileIsPresent() {
        List<IDownloadedFile> mockList = new LinkedList<>();
        Reporter reporter = Reporter.getSingleton();
        reporter.setUncompletedFiles(mockList);
        reporter.getReport();
        List<IDownloadedFile> result = reporter.getUncompletedFiles();
        assertEquals(0, result.size());
    }
}
