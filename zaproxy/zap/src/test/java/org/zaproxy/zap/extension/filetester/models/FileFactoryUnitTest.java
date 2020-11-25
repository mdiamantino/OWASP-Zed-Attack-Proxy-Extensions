package org.zaproxy.zap.extension.filetester.models;

import org.junit.jupiter.api.Test;
import org.zaproxy.zap.extension.filetester.models.fileTypes.ExeFile;
import org.zaproxy.zap.extension.filetester.models.fileTypes.ZipFile;
import org.zaproxy.zap.extension.filetester.models.fileTypes.imageModels.JpegFile;
import org.zaproxy.zap.extension.filetester.models.fileTypes.imageModels.PngFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileFactoryUnitTest {
    @Test
    public void pngFileCreation() throws IOException {
        assertEquals(PngFile.class, FileTesterUnitTestHelper.createFile("pngCorrect.png").getClass());
    }

    @Test
    public void jpegFileObjectCreation() throws IOException {
        assertEquals(JpegFile.class, FileTesterUnitTestHelper.createFile("jpegCorrect.jpeg").getClass());
    }

    @Test
    public void jpgFileObjectCreation() throws IOException {
        assertEquals(JpegFile.class, FileTesterUnitTestHelper.createFile("jpegExif.jpg").getClass());
    }

    @Test
    public void zipFileObjectCreation() throws IOException {
        assertEquals(ZipFile.class, FileTesterUnitTestHelper.createFile("zipCorrect.zip").getClass());
    }

    @Test
    public void exeFileObjectCreation() throws IOException {
        assertEquals(ExeFile.class, FileTesterUnitTestHelper.createFile("exeTestSample.exe").getClass());
    }

    @Test
    public void invalidObjectCreation() {
        assertThrows(IllegalArgumentException.class, () -> FileTesterUnitTestHelper.createFile("invalid.extension"));
    }
}
