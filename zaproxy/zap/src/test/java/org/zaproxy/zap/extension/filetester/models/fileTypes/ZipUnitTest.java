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
import org.zaproxy.zap.extension.filetester.models.FileTesterUnitTestHelper;
import org.zaproxy.zap.extension.filetester.models.IDownloadedFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZipUnitTest {
    @Test
    public void zipFileWithCorrectData() throws IOException {
        IDownloadedFile zipFile = FileTesterUnitTestHelper.createFile("zipCorrect.zip");
        assertTrue(zipFile.isValid());
        assertTrue(zipFile.isCompleted());
    }

    @Test
    public void zipFileWithEncryption() throws IOException {
        IDownloadedFile zipFile = FileTesterUnitTestHelper.createFile("zipEncrypted.zip");
        assertFalse(zipFile.isValid());
        assertTrue(zipFile.isCompleted());
    }

    @Test
    public void zipFileWithPathTraversalVulnerability() throws IOException {
        IDownloadedFile zipFile = FileTesterUnitTestHelper.createFile("zipPathVul.zip");
        assertFalse(zipFile.isValid());
        assertTrue(zipFile.isCompleted());
    }

    @Test
    public void zipFileWithBomb() throws IOException {
        IDownloadedFile zipFile = FileTesterUnitTestHelper.createFile("zipBomb.zip");
        assertFalse(zipFile.isValid());
        assertTrue(zipFile.isCompleted());
    }
}
