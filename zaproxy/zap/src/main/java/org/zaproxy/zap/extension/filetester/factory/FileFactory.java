package org.zaproxy.zap.extension.filetester.factory;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.zaproxy.zap.extension.filetester.model.*;

import java.io.File;

public class FileFactory {

    private static final Logger logger = Logger.getLogger(FileFactory.class);

    public IDownloadedFile createdDownloadedFile(String pathname) {
        String fileExtension = FilenameUtils.getExtension(pathname).toLowerCase();
        File file = new File(pathname);
        switch (fileExtension) {
            case "jpg":
            case "jpeg":
                return new JpegFile(file);
            case "png":
                return new PngFile(file);
            case "zip":
                return new ZipFile(file);
            case "exe":
                return new ExeFile(file);
            default:
                throw new IllegalArgumentException("Unknown extension type");
        }
    }
}
