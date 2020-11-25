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
package org.zaproxy.zap.extension.filetester.models.fileTypes.imageModels;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifReader;
import net.sf.json.JSONObject;
import org.zaproxy.zap.extension.filetester.models.FileTestResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * JpegFile manages the validation of JPEG file type and EXIF metadata extraction.
 */
public class JpegFile extends ImageFile {

    private static final int JPEG_FILE_SIGNATURE = 0xffd8ffe0;
    private static final String TEST_JPEG_VALIDATION = "Jpeg Invalidation";
    private static final String TEST_EXIF_EXTRACTION = "EXIF Metadata Extraction";

    private Map<String, String> extractedMetadata;

    public JpegFile(String name, InputStream file) {
        super(name, file);
        extractedMetadata = new HashMap<>();
    }

    @Override
    public boolean isValid() throws IOException {
        boolean isValid = false;
        FileTestResult fileInvalidation = new FileTestResult(TEST_JPEG_VALIDATION);
        if (isValid(JPEG_FILE_SIGNATURE)) {
            this.getFile().reset();
            fileInvalidation.setResult(false);
            FileTestResult metadataExtraction = new FileTestResult(TEST_EXIF_EXTRACTION);
            extractMetadata();
            if (extractedMetadata.isEmpty()) {
                isValid = true;
                metadataExtraction.setResult(false);
            } else {
                metadataExtraction.setResult(true);
                metadataExtraction.setRemarks(JSONObject.fromObject(extractedMetadata).toString());
            }
            this.getTestResults().add(metadataExtraction);
            fileInvalidation.setRemarks("The file passed the test.");
        } else {
            fileInvalidation.setResult(true);
            fileInvalidation.setRemarks("File invalid");
        }
        this.getTestResults().add(fileInvalidation);
        isCompleted = true;
        return isValid;
    }

    /**
     * Extracts the EXIF metadata from the file.
     *
     * @throws IOException if cannot read the input stream.
     */
    private void extractMetadata() throws IOException {
        Iterable<JpegSegmentMetadataReader> readers = Collections.singletonList(new ExifReader());
        Metadata metadata;
        try {
            metadata = JpegMetadataReader.readMetadata(this.getFile(), readers);
            for (Directory directory : metadata.getDirectories()) {
                if (!directory.toString().equals("File Directory (3 tags)")) {
                    for (Tag tag : directory.getTags()) {
                        extractedMetadata.put(tag.getTagName(), tag.getDescription());
                    }
                }
            }
        } catch (JpegProcessingException ignore) {
            // Ignore
        }
    }
}
