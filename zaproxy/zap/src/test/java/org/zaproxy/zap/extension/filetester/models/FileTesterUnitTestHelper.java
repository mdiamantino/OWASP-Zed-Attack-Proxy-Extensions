package org.zaproxy.zap.extension.filetester.models;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FileTesterUnitTestHelper {
    private static final String directory = System.getProperty("user.dir") + "/src/test/resources/org/zaproxy/zap/extension/filetester/";

    public static IDownloadedFile createFile(String filePath) throws IOException {
        FileFactory fileFactory = new FileFactory();
        File file = new File(directory + filePath);
        InputStream stream = new ByteArrayInputStream(Files.readAllBytes(file.toPath()));
        return fileFactory.createdDownloadedFile(filePath, stream);
    }
}
