package net.twasi.core.api.upload;

import net.twasi.core.database.models.User;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.providers.JWTService;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

@MultipartConfig(location = "tmp", fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public abstract class UploadHandlerBase extends HttpServlet {

    public UploadHandlerBase() {
        File uploadDir = new File(UploadHandlerBase.uploadDir);
        File handlerDir = new File(getFullSubDir());
        if (!uploadDir.exists()) uploadDir.mkdir();
        if (!handlerDir.exists()) handlerDir.mkdir();
    }

    private static String uploadDir = "uploads";

    public String getSubDir() {
        return "/";
    }

    public abstract void validateFile(Part file) throws Exception;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");

        try {

            User user;
            try {
                String jwt = req.getParameter("jwt");
                user = JWTService.get().getManager().getUserFromToken(jwt);
                if (user == null) throw new RuntimeException();
            } catch (Exception e) {
                resp.sendError(401, "You need to provide a valid jwt token to upload a file.");
                return;
            }

            Part file = req.getPart("file");
            if (file == null) {
                resp.sendError(400, "You need to add a file to your request (name: 'file').");
                return;
            }

            try {
                validateFile(file);
            } catch (Exception e) {
                resp.sendError(400, "Please send a valid file (" + e.getMessage() + ").");
                return;
            }

            String fileName = getFileName(file);
            String newFileName = user.getTwitchAccount().getTwitchId() + ':'
                    + new SimpleDateFormat("yyyy:MM:DD-HH:mm:ss").format(Calendar.getInstance().getTime()) + ':'
                    + fileName.trim().replaceAll("\\s+", "_").replaceAll("^[a-zA-Z0-9\\-_.+]", "");

            String subDir = getFullSubDir();

            InputStream stream = file.getInputStream();
            copyInputStreamToFile(stream, new File(subDir + fileName));

            resp.setStatus(200);
            resp.setHeader("filePath", subDir + newFileName);
        } catch (Exception e) {
            resp.sendError(500);
            TwasiLogger.log.error("Upload Error", e);
        }
    }

    private String getFullSubDir() {
        String subdir = getSubDir();
        if (!subdir.startsWith("/")) subdir = "/" + subdir;
        if (subdir.endsWith("/")) subdir = subdir.substring(0, subdir.length() - 1);
        subdir = uploadDir + subdir;
        return subdir;
    }

    protected static String getFileName(Part file) {
        return Paths.get(file.getSubmittedFileName()).getFileName().toString();
    }

    private static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }

    protected static void checkSize(Part file, int MiB) {
        long allowed = MiB * 8389000;
        if (file.getSize() > allowed)
            throw new RuntimeException("Please don't exceed the file size limit of " + MiB + "MiB.");
    }

    protected static void checkType(Part file, String... endings) {
        if (Arrays.stream(endings).anyMatch(ending -> getFileName(file).toLowerCase().endsWith(ending.toLowerCase())))
            throw new RuntimeException("Please upload a [" + String.join(", ", endings) + "] file.");
    }
}
