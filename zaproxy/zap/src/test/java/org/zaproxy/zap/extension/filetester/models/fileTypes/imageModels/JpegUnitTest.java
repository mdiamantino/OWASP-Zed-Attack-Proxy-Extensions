package org.zaproxy.zap.extension.filetester.models.fileTypes.imageModels;

import org.junit.jupiter.api.Test;
import org.zaproxy.zap.extension.filetester.models.FileTesterUnitTestHelper;
import org.zaproxy.zap.extension.filetester.models.IDownloadedFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JpegUnitTest {

    @Test
    public void jpegFileWithCorrectSignature() throws IOException {
        IDownloadedFile jpegFile = FileTesterUnitTestHelper.createFile("jpegCorrect.jpeg");
        assertTrue(jpegFile.isValid());
        assertTrue(jpegFile.isCompleted());
    }

    @Test
    public void jpegFileWithIncorrectSignature() throws IOException {
        IDownloadedFile jpegFile = FileTesterUnitTestHelper.createFile("jpegIncorrect.jpg");
        assertFalse(jpegFile.isValid());
        assertTrue(jpegFile.isCompleted());
    }

    @Test
    public void jpegFileWithExifData() throws IOException {
        IDownloadedFile jpegFile = FileTesterUnitTestHelper.createFile("jpegExif.jpg");
        assertFalse(jpegFile.isValid());
        assertTrue(jpegFile.isCompleted());
    }
}
