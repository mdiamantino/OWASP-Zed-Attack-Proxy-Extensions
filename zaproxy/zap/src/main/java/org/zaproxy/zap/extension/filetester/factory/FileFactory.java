package org.zaproxy.zap.extension.filetester.factory;

import org.apache.log4j.Logger;
import org.zaproxy.zap.extension.filetester.model.*;

import java.io.File;

public class FileFactory {

    private static final Logger logger = Logger.getLogger(FileFactory.class);

    private static String getExtension(String fileName) {
        char ch;
        int len;
        if (fileName == null || (len = fileName.length()) == 0 || (ch = fileName.charAt(len - 1)) == '/' || ch == '\\'
                || // in the case of a directory
                ch == '.') // in the case of . or ..
            return "";
        int dotInd = fileName.lastIndexOf('.'),
                sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        if (dotInd <= sepInd)
            return "";
        else
            return fileName.substring(dotInd + 1).toLowerCase();
    }

    public IDownloadedFile createdDownloadedFile(String pathname) {
        String fileExtension = getExtension(pathname).toLowerCase();
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
