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
