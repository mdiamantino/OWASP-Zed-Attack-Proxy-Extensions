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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FileTesterUnitTestHelper {
    private static final String directory = System.getProperty("user.dir") + "/src/test/resources/org/zaproxy/zap/extension/filetester/";

    public static IDownloadedFile createFile(String filePath) throws IOException {
        File file = new File(directory + filePath);
        InputStream stream = new ByteArrayInputStream(Files.readAllBytes(file.toPath()));
        return FileFactory.createdDownloadedFile(filePath, stream);
    }
}
