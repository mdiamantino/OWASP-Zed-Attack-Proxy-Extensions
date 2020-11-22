package org.zaproxy.zap.extension.filetester.model;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifReader;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JpegFile extends ImageFile {

    private static final int JPEG_MAGIC_NUMBER = 0xffd8ffe0;

    private Map<String, String> extractedMetadata;

    public JpegFile(String name, InputStream file) {
        super(name, file);
        extractedMetadata = new HashMap<>();
    }

    @Override
    public boolean isValid() throws IOException {
        boolean isValid = false;
        FileTestResult fileInvalidation = new FileTestResult("Jpeg Invalidation");
        if (isValid(JPEG_MAGIC_NUMBER)) {
            this.getFile().reset();
            isValid = true;
            fileInvalidation.setResult(false);
            try {
                FileTestResult metadataExtraction = new FileTestResult("EXIF MetaData Extraction");
                extractMetadata();
                if (extractedMetadata.isEmpty()) {
                    metadataExtraction.setResult(false);
                } else {
                    metadataExtraction.setResult(true);
                    metadataExtraction.setRemarks(JSONObject.fromObject(extractedMetadata).toString());
                }
                this.getTestResults().add(metadataExtraction);
            } catch (JpegProcessingException e) {
                e.printStackTrace();
            }
            fileInvalidation.setRemarks("");
        } else {
            fileInvalidation.setResult(true);
            fileInvalidation.setRemarks("File invalid");
        }
        this.getTestResults().add(fileInvalidation);
        return isValid;
    }

    private void extractMetadata() throws JpegProcessingException, IOException {
        Iterable<JpegSegmentMetadataReader> readers = Arrays.asList(new ExifReader());
        Metadata metadata = JpegMetadataReader.readMetadata(this.getFile(), readers);
        for (Directory directory : metadata.getDirectories()) {
            if (!directory.toString().equals("File Directory (3 tags)")) {
                for (Tag tag : directory.getTags()) {
                    extractedMetadata.put(tag.getTagName(), tag.getDescription());
                }
            }
        }
    }

}
