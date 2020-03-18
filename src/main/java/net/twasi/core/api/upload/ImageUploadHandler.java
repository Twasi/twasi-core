package net.twasi.core.api.upload;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

@MultipartConfig(location = "tmp", fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class ImageUploadHandler extends UploadHandlerBase {

    @Override
    public String getSubDir() {
        return "IMG";
    }

    @Override
    public void validateFile(Part file) throws Exception {
        checkSize(file, 6);
        checkType(file, ".jpg", ".png", ".gif");
    }
}
