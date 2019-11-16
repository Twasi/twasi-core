package net.twasi.core.api.upload;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

@MultipartConfig(location = "tmp", fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class CSVUploadHandler extends UploadHandlerBase {

    @Override
    public String getSubDir() {
        return "CSV";
    }

    @Override
    public void validateFile(Part file) throws Exception {
        throw new NotImplementedException();
    }
}
