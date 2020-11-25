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

import org.apache.commons.io.FileUtils;
import org.parosproxy.paros.Constant;
import org.zaproxy.zap.extension.filetester.models.DownloadedFile;
import org.zaproxy.zap.extension.filetester.models.FileTestResult;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * ZipFile manages the validation of ZIP file type by checking if the file is encrypted, a zip bomb or contains a path traversal vulnerability.
 */
public class ZipFile extends DownloadedFile {

    private static final String TEST_ZIP_ENCRYPTION_DETECTION = "Encryption Detection";
    private static final String TEST_ZIP_BOMB_DETECTION = "Zip Bomb Detection";
    private static final String TEST_ZIP_PATH_TRAVERSAL_DETECTION = "Path Traversal Detection";

    private static final int BUFFER = 512;
    private static final long TOO_BIG = 0x6400000; // Max size of unzipped data, 100MB
    private static final int TOO_MANY = 1024; // Max number of files

    public ZipFile(String name, InputStream file) {
        super(name, file);
    }

    @Override
    public boolean isValid() throws IOException {
        boolean isValid = isNotPasswordProtected() && isNotZIPBomb();
        isCompleted = true;
        return isValid;
    }

    /**
     * Checks if the file is not password protected.
     *
     * @return True if the file is not password protected.
     * @throws IOException if cannot read the input stream.
     */
    private boolean isNotPasswordProtected() throws IOException {
        File file = new File(this.getName());
        FileUtils.copyInputStreamToFile(this.getFile(), file);
        net.lingala.zip4j.ZipFile zipFile = new net.lingala.zip4j.ZipFile(file);
        boolean isEncrypted = zipFile.isEncrypted();
        FileTestResult encryptionDetection = new FileTestResult(TEST_ZIP_ENCRYPTION_DETECTION);
        encryptionDetection.setResult(isEncrypted);
        encryptionDetection.setRemarks((isEncrypted?"The file is encrypted." : "The file passed the test."));
        this.getTestResults().add(encryptionDetection);
        Files.deleteIfExists(Paths.get(this.getName()));
        return !isEncrypted;
    }

    /**
     * Check if the file is not a ZIP Bomb.
     *
     * @return True if the file is not a ZIP bomb.
     * @throws IOException if cannot read the input stream.
     */
    // TODO SPLIT
    private boolean isNotZIPBomb() throws IOException {
        this.getFile().reset();
        boolean isValid = true;
        FileTestResult zipBombDetection = new FileTestResult(TEST_ZIP_BOMB_DETECTION);
        FileTestResult pathTraversalDetection = new FileTestResult(TEST_ZIP_PATH_TRAVERSAL_DETECTION);
        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(this.getFile()))) {
            ZipEntry entry;
            int entries = 0;
            long total = 0;
            while ((entry = zis.getNextEntry()) != null) {
                int count;
                byte[] data = new byte[BUFFER];
                if (!doesNotContainPathTraversalInfo(entry.getName())) {
                    pathTraversalDetection.setResult(true);
                    pathTraversalDetection.setRemarks("File contains path traversal vulnerabilities.");
                    this.getTestResults().add(pathTraversalDetection);
                    return false;
                }
                while (total + BUFFER <= TOO_BIG && (count = zis.read(data, 0, BUFFER)) != -1) {
                    total += count;
                }
                zis.closeEntry();
                entries++;
                if (entries > TOO_MANY) {
                    throw new IllegalStateException("Too many files to unzip.");
                }
                if (total + BUFFER > TOO_BIG) {
                    throw new IllegalStateException("File being unzipped is too big.");
                }
            }
            zipBombDetection.setResult(false);
            zipBombDetection.setRemarks("The file passed the test.");
            this.getTestResults().add(zipBombDetection);
            pathTraversalDetection.setResult(false);
            pathTraversalDetection.setRemarks("The file passed the test.");
            this.getTestResults().add(pathTraversalDetection);
        } catch (IllegalStateException ise) {
            isValid = false;
            pathTraversalDetection.setResult(true);
            pathTraversalDetection.setRemarks("File contains path traversal vulnerabilities.");
            this.getTestResults().add(pathTraversalDetection);
            zipBombDetection.setResult(true);
            zipBombDetection.setRemarks("File is a ZIP bomb.");
            this.getTestResults().add(zipBombDetection);
        }
        return isValid;
    }

    /**
     * Check if the downloaded ZIP file does not contain a file with path traversal vulnerability.
     *
     * @param filename The name of the file in the ZIP file.
     * @return True if it does not contain the vulnerability.
     * @throws IOException if cannot read the input stream.
     */
    private boolean doesNotContainPathTraversalInfo(String filename) throws IOException {
        File file = new File(filename);
        String canonicalPath = file.getCanonicalPath();
        File iD = new File(".");
        String canonicalID = iD.getCanonicalPath();
        return canonicalPath.startsWith(canonicalID);
    }
}
