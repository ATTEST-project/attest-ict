package com.attest.ict.custom.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    // list variable that contains content types of file
    private static final List<String> contentTypes = Arrays.asList(
        "application/vnd.oasis.opendocument.spreadsheet",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    private static final List<String> T51ContentTypes = Arrays.asList(
        "text/csv",
        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    private static final List<String> T51MonitoringContentTypes = Arrays.asList(
        "text/csv",
        "application/json",
        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    public static String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return getFileExtension(fileName);
    }

    public static String getFileExtension(File file) {
        String ext = "";
        String fileName = file.getName();
        return getFileExtension(fileName);
    }

    public static String getFileExtension(String fileName) {
        String ext = "";
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            ext = fileName.substring(index + 1);
        }
        return ext;
    }

    public static String getFileLessExtension(String fileName) {
        String name = "";
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            name = fileName.substring(0, index);
        }
        return name;
    }

    public static String probeContentType(Path path) throws IOException {
        File file = path.toFile();
        String contentType = "";
        String extension = getFileExtension(file);
        switch (extension) {
            case "ods":
                contentType = "application/vnd.oasis.opendocument.spreadsheet";
                break;
            case "json":
                contentType = "application/json";
                break;
            case "xls":
                contentType = "application/vnd.ms-excel";
                break;
            case "xlsx":
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                break;
            case "csv":
                contentType = "text/csv";
                break;
            case "txt":
                contentType = "text/plain";
                break;
            case "html":
            case "htm":
                contentType = "text/html";
                break;
            case "pdf":
                contentType = "application/pdf";
                break;
            case "doc":
            case "docx":
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                break;
            case "h5":
                contentType = "text/x-hdf5";
                break;
            case "m":
                contentType = "application/octet-stream";
        }
        return contentType;
    }

    /*
     * method to extract files and folders from a zip file
     */
    public static void unzipFile(MultipartFile file, String dir) throws IOException {
        File destDir = new File(dir);
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(file.getInputStream());
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    /*
     * creation of new file useful for 'unzipFile' method
     */
    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    /*
     * method to copy a file to a particular path
     */
    public static void moveFileTo(MultipartFile file, String path) throws IOException {
        String inputFile = path + file.getOriginalFilename();

        Files.copy(file.getInputStream(), Paths.get(inputFile), StandardCopyOption.REPLACE_EXISTING);
    }

    /*
     * method to copy a file from a path to another one
     */
    public static void moveFileFromTo(String from, String to) throws IOException {
        Files.copy(Paths.get(from), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
    }

    /*
     * check content type of a file
     */
    public static boolean checkContentType(MultipartFile file) {
        return contentTypes.contains(file.getContentType());
    }

    public static boolean checkContentTypeT51(MultipartFile file) {
        return T51ContentTypes.contains(file.getContentType());
    }

    public static boolean checkContentTypeT51Monitoring(MultipartFile file) {
        return T51MonitoringContentTypes.contains(file.getContentType());
    }

    public static boolean isDotH5File(MultipartFile file) {
        String ext = "";
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            ext = fileName.substring(index + 1);
        }
        return ext.equals("h5");
    }

    /*
     * delete a file from path
     */
    public static boolean removeFileFromPath(String filePath) {
        boolean isDeleted = new File(filePath).delete();
        if (isDeleted) LOGGER.debug("File {}, deleted! ", filePath);

        return isDeleted;
    }

    /**
     * @param tempFile to delete
     * @return true if file is deleted, false otherwise
     * @throws IOException
     */
    public static boolean deleteFile(File file) throws IOException {
        boolean isDeleted = false;

        if (file.exists()) {
            isDeleted = file.delete();
        }

        if (isDeleted) {
            LOGGER.debug("File {}, deleted! ", file.getPath());
        }
        return isDeleted;
    }

    /*
     * method to create zip file with a list of files
     */
    public static File zip(List<File> files, String fileName) {
        File zipFile = new File(fileName);
        // Create a buffer for reading the files
        byte[] buf = new byte[1024];
        try {
            // create the ZIP file
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
            // compress the files
            for (File file : files) {
                FileInputStream in = new FileInputStream(file.getCanonicalPath());
                // add ZIP entry to output stream
                out.putNextEntry(new ZipEntry(file.getName()));
                // transfer bytes from the file to the ZIP file
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                // complete the entry
                out.closeEntry();
                in.close();
            }
            // complete the ZIP file
            out.close();
            return zipFile;
        } catch (IOException e) {
            LOGGER.error("Exception: ", e);
        }
        return null;
    }

    /*
     * write script logs to file
     */
    public static File writeLogsToFile(BufferedReader reader, String filePath) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            String line = "";
            while ((line = reader.readLine()) != null) {
                LOGGER.info(line);
                writer.write(line);
                writer.newLine();
            }
            writer.close();
            return new File(filePath);
        } catch (IOException e) {
            LOGGER.error("Exception: ", e);
            return null;
        }
    }

    public static File writeJuliaLogsToFile(BufferedReader reader, String filePath) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (!(line.contains("Warning") || line.contains("@ "))) {
                    LOGGER.info(line);
                    writer.write(line);
                    writer.newLine();
                }
            }
            writer.close();
            return new File(filePath);
        } catch (IOException e) {
            LOGGER.error("Exception: ", e);
            return null;
        }
    }

    public static File writeToolLogsToFile(InputStreamReader inputStreamReader, String filePath) {
        BufferedReader reader = new BufferedReader(inputStreamReader);
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (!(line.contains("Warning") || line.contains("@ "))) {
                    LOGGER.info(line);
                    writer.write(line);
                    writer.newLine();
                }
            }
            writer.close();
            return new File(filePath);
        } catch (IOException e) {
            LOGGER.error("Exception writing file: " + filePath, e);
            return null;
        }
    }

    public static void createDir(String directory) {
        try {
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
                LOGGER.info(" Directory {} created ", directory);
            }
        } catch (Exception ex) {
            LOGGER.error("Exception: ", ex);
        }
    }

    public static void main(String args[]) { //static method
        //File file = new File ("C:\\temp\\2_hr_dx_01_2020_red_output.xlsx");
        // File file = new File ("C:\\temp\\branches_location1.csv");
        //File file = new File ("C:\\temp\\branchExtension.json");
        //File file = new File ("C:\\temp\\response.ods");
        //File file = new File ("C:\\temp\\Per Usare Zoom.pdf");
        //File file = new File ("C:\\temp\\Installazione_DOC.docx");
        //File file = new File ("C:\\temp\\modelTR1.h5");
        File file = new File("C:\\temp\\A_KPC_35.m");

        Path path = file.toPath();
        String ext = FileUtils.getFileExtension(file);
        try {
            String mimeType = FileUtils.probeContentType(path);
            String fromProbe = Files.probeContentType(path);
            System.out.println("ext " + ext + " contentType " + mimeType + " fromProbe " + fromProbe);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String fileName = "A_KPC_35.m";
        String name = FileUtils.getFileLessExtension(fileName);
        System.out.println("name " + name);
    }
}
