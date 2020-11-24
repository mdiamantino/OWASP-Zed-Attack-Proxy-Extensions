package org.zaproxy.zap.extension.filetester.factory;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.zaproxy.zap.extension.filetester.model.*;

import java.io.File;
import java.io.InputStream;

public class FileFactory {

    private static final Logger logger = Logger.getLogger(FileFactory.class);

    public IDownloadedFile createdDownloadedFile(String name, InputStream file) {
        String fileExtension = FilenameUtils.getExtension(name).toLowerCase();
        switch (fileExtension) {
            case "jpg":
            case "jpeg":
                return new JpegFile(name, file);
            case "png":
                return new PngFile(name, file);
            case "zip":
                return new ZipFile(name, file);
            case "exe":
                return new ExeFile(name, file);
            default:
                throw new IllegalArgumentException("Unknown extension type");
        }
    }
}
