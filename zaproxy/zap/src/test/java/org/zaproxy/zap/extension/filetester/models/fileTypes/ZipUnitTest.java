package org.zaproxy.zap.extension.filetester.models.fileTypes;

import org.junit.jupiter.api.Test;
import org.zaproxy.zap.extension.filetester.FileTesterUnitTestHelper;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZipUnitTest {
    @Test
    public void zipFileWithCorrectData() throws IOException {
        ZipFile zipFile = (ZipFile) FileTesterUnitTestHelper.createFile("zipCorrect.zip");
        assertTrue(zipFile.isValid());
        assertTrue(zipFile.isCompleted());
    }

    @Test
    public void zipFileWithEncryption() throws IOException {
        ZipFile zipFile = (ZipFile) FileTesterUnitTestHelper.createFile("zipEncrypted.zip");
        assertFalse(zipFile.isValid());
        assertTrue(zipFile.isCompleted());
    }

    @Test
    public void zipFileWithPathTraversalVulnerability() throws IOException {
        ZipFile zipFile = (ZipFile) FileTesterUnitTestHelper.createFile("zipPathVul.zip");
        assertFalse(zipFile.isValid());
        assertTrue(zipFile.isCompleted());
    }

    @Test
    public void zipFileWithBomb() throws IOException {
        ZipFile zipFile = (ZipFile) FileTesterUnitTestHelper.createFile("zipBomb.zip");
        assertFalse(zipFile.isValid());
        assertTrue(zipFile.isCompleted());
    }
}
