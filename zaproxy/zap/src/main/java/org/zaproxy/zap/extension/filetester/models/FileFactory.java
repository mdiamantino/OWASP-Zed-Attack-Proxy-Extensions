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

import org.apache.commons.io.FilenameUtils;
import org.zaproxy.zap.extension.filetester.models.fileTypes.ExeFile;
import org.zaproxy.zap.extension.filetester.models.fileTypes.ZipFile;
import org.zaproxy.zap.extension.filetester.models.fileTypes.imageModels.JpegFile;
import org.zaproxy.zap.extension.filetester.models.fileTypes.imageModels.PngFile;

import java.io.InputStream;

/**
 * FileFactory manages the creation of object based on a file's extension.
 */
public class FileFactory {
    /**
     * Creates an object of IDownloadedFile based on a file's extension.
     *
     * @param name            Name of the file
     * @param fileInputStream Stream of the file
     * @return Object of a class that implements IDownloaded. Currently supported file types are JPEG, PNG, ZIP, EXE.
     */
    public static IDownloadedFile createdDownloadedFile(String name, InputStream fileInputStream) {
        String fileExtension = FilenameUtils.getExtension(name).toLowerCase();
        switch (fileExtension) {
            case "jpg":
            case "jpeg":
                return new JpegFile(name, fileInputStream);
            case "png":
                return new PngFile(name, fileInputStream);
            case "zip":
                return new ZipFile(name, fileInputStream);
            case "exe":
                return new ExeFile(name, fileInputStream);
            default:
                throw new IllegalArgumentException("Unknown extension type");
        }
    }
}
