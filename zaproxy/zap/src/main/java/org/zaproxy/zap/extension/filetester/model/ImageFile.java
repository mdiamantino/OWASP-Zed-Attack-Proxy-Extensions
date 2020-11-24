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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ImageFile manages the validation of image file types.
 */
public abstract class ImageFile extends DownloadedFile {

    public ImageFile(String name, InputStream file) {
        super(name, file);
    }

    /**
     * Checks if the image file contains the image signature associated with the image type.
     *
     * @param imageSignature Image signature of the image file type.
     * @return True if the image signature is validated in the image.
     * @throws IOException if cannot read the input stream.
     */
    protected boolean isValid(int imageSignature) throws IOException {
        try (DataInputStream ins = new DataInputStream(new BufferedInputStream(this.getFile()))) {
            return ins.readInt() == imageSignature;
        }
    }
}
