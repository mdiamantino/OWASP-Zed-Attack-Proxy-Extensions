package org.zaproxy.zap.extension.filetester.models.fileTypes.imageModels;

import org.junit.jupiter.api.Test;
import org.zaproxy.zap.extension.filetester.FileTesterUnitTestHelper;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JpegUnitTest {

    @Test
    public void jpegFileWithCorrectSignature() throws IOException {
        JpegFile jpegFile = (JpegFile) FileTesterUnitTestHelper.createFile("jpegCorrect.jpeg");
        assertTrue(jpegFile.isValid());
        assertTrue(jpegFile.isCompleted());
    }

    @Test
    public void jpegFileWithIncorrectSignature() throws IOException {
        JpegFile jpegFile = (JpegFile) FileTesterUnitTestHelper.createFile("jpegIncorrect.jpg");
        assertFalse(jpegFile.isValid());
        assertTrue(jpegFile.isCompleted());
    }

    @Test
    public void jpegFileWithExifData() throws IOException {
        JpegFile jpegFile = (JpegFile) FileTesterUnitTestHelper.createFile("jpegExif.jpg");
        assertFalse(jpegFile.isValid());
        assertTrue(jpegFile.isCompleted());
    }
}
