package org.zaproxy.zap.extension.filetester.models.fileTypes.imageModels;

import org.junit.jupiter.api.Test;
import org.zaproxy.zap.extension.filetester.models.FileTesterUnitTestHelper;
import org.zaproxy.zap.extension.filetester.models.IDownloadedFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PngUnitTest {

    @Test
    public void pngFileWithCorrectSignature() throws IOException {
        IDownloadedFile pngFile = FileTesterUnitTestHelper.createFile("pngCorrect.png");
        assertTrue(pngFile.isValid());
        assertTrue(pngFile.isCompleted());
    }

    @Test
    public void pngFileWithIncorrectSignature() throws IOException {
        IDownloadedFile pngFile = FileTesterUnitTestHelper.createFile("pngIncorrect.png");
        assertFalse(pngFile.isValid());
        assertTrue(pngFile.isCompleted());
    }
}
