package org.zaproxy.zap.extension.filetester.model;

import net.lingala.zip4j.exception.ZipException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipFile extends DownloadedFile {
    private static final int BUFFER = 512;
    private static final long TOOBIG = 0x6400000; // Max size of unzipped data, 100MB
    private static final int TOOMANY = 1024; // Max number of files

    public ZipFile(File file) {
        super(file);
    }

    @Override
    public boolean isValid() throws ZipException, IOException {
        boolean isValid = isNotPasswordProtected() && isNotZIPBomb();
        return isValid;
    }

    private boolean isNotPasswordProtected() throws ZipException {
        net.lingala.zip4j.ZipFile zipFile = new net.lingala.zip4j.ZipFile(this.getFile());
        boolean isEncrypted = zipFile.isEncrypted();
        FileTestResult encryptionDetection = new FileTestResult("Encryption Detection");
        if (isEncrypted) {
            encryptionDetection.setResult(true);
        } else {
            encryptionDetection.setResult(false);
        }
        this.getTestResults().add(encryptionDetection);
        return !isEncrypted;
    }

    private boolean isNotZIPBomb() throws IOException, IllegalArgumentException {
        boolean isValid = true;
        FileTestResult zipBombDetection = new FileTestResult("Zip Bomb Detection");
        FileTestResult pathTraversalDetection = new FileTestResult("Path Traversal Detection");
        FileInputStream fis = new FileInputStream(this.getFile());
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
        ZipEntry entry;
        int entries = 0;
        long total = 0;
        try {
            while ((entry = zis.getNextEntry()) != null) {
                int count;
                byte data[] = new byte[BUFFER];
                String name = doesNotContainPathTraversalInfo(entry.getName(), ".");
//                if (entry.isDirectory()) {
//                    System.out.println("Creating directory " + name);
//                    new File(name).mkdir();
//                    continue;
//                }
                while (total + BUFFER <= TOOBIG && (count = zis.read(data, 0, BUFFER)) != -1) {
                    total += count;
                }
                zis.closeEntry();
                entries++;
                if (entries > TOOMANY) {
                    throw new IllegalStateException("Too many files to unzip.");
                }
                if (total + BUFFER > TOOBIG) {
                    throw new IllegalStateException("File being unzipped is too big.");
                }
            }
            zipBombDetection.setResult(false);
            this.getTestResults().add(zipBombDetection);
            pathTraversalDetection.setResult(false);
            this.getTestResults().add(pathTraversalDetection);
        } catch (IllegalArgumentException iae) {
            pathTraversalDetection.setResult(true);
            this.getTestResults().add(pathTraversalDetection);
            isValid = false;
        } catch (IllegalStateException ise) {
            isValid = false;
            pathTraversalDetection.setResult(true);
            this.getTestResults().add(pathTraversalDetection);
            zipBombDetection.setResult(true);
            this.getTestResults().add(zipBombDetection);
        } finally {
            zis.close();
        }
        return isValid;
    }

    private String doesNotContainPathTraversalInfo(String filename, String intendedDir) throws java.io.IOException {
        File f = new File(filename);
        String canonicalPath = f.getCanonicalPath();

        File iD = new File(intendedDir);
        String canonicalID = iD.getCanonicalPath();

        if (canonicalPath.startsWith(canonicalID)) {
            return canonicalPath;
        } else {
            throw new IllegalArgumentException("File is outside extraction target directory.");
        }
    }
}
