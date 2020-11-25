package org.zaproxy.zap.extension.filetester.models.fileTypes.imageModels;

import org.junit.jupiter.api.Test;
import org.zaproxy.zap.extension.filetester.FileTesterUnitTestHelper;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PngUnitTest {

    @Test
    public void pngFileWithCorrectSignature() throws IOException {
        PngFile pngFile = (PngFile) FileTesterUnitTestHelper.createFile("pngCorrect.png");
        assertTrue(pngFile.isValid());
        assertTrue(pngFile.isCompleted());
    }

    @Test
    public void pngFileWithIncorrectSignature() throws IOException {
        PngFile pngFile = (PngFile) FileTesterUnitTestHelper.createFile("pngIncorrect.png");
        assertFalse(pngFile.isValid());
        assertTrue(pngFile.isCompleted());
    }
}
