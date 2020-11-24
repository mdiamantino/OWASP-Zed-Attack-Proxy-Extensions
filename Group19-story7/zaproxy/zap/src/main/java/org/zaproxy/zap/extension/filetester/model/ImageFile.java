package org.zaproxy.zap.extension.filetester.model;

import java.io.*;

public abstract class ImageFile extends DownloadedFile {

    public ImageFile(String name, InputStream file) {
        super(name, file);
    }

    protected boolean isValid(int magicNumber) throws IOException {
        DataInputStream ins = new DataInputStream(new BufferedInputStream(this.getFile()));
        try {
            return ins.readInt() == magicNumber;
        } finally {
            ins.close();
        }
    }
}
