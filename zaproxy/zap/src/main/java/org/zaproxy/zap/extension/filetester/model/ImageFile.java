package org.zaproxy.zap.extension.filetester.model;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class ImageFile extends DownloadedFile {
    public ImageFile(File file) {
        super(file);
    }

    protected boolean isValid(int magicNumber) throws IOException {
        DataInputStream ins = new DataInputStream(new BufferedInputStream(new FileInputStream(this.getFile())));
        try {
            return ins.readInt() == magicNumber;
        } finally {
            ins.close();
        }
    }
}
