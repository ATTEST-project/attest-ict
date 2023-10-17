package com.attest.ict.custom.utils;

import com.attest.ict.domain.OutputFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

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

    public static final Map<String, String> CONTENT_TYPE = new HashMap<>();

    static {
        CONTENT_TYPE.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        CONTENT_TYPE.put("json", "application/json");
        CONTENT_TYPE.put("xls", "application/vnd.ms-excel");
        CONTENT_TYPE.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        CONTENT_TYPE.put("csv", "text/csv");
        CONTENT_TYPE.put("txt", "text/plain");
        CONTENT_TYPE.put("html", "text/html");
        CONTENT_TYPE.put("htm", "text/html");
        CONTENT_TYPE.put("pdf", "application/pdf");
        CONTENT_TYPE.put("doc", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        CONTENT_TYPE.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        CONTENT_TYPE.put("zip", "application/zip");
        CONTENT_TYPE.put("zip-compressed", "application/x-zip-compressed");
        CONTENT_TYPE.put("h5", "application/octet-stream");
        CONTENT_TYPE.put("m", "application/octet-stream");
    }

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
        String extension = getFileExtension(file);
        return CONTENT_TYPE.get(extension);
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
     * delete a file from path
     */
    public static boolean removeFileFromPath(String filePath) {
        boolean isDeleted = new File(filePath).delete();
        if (isDeleted) log.debug("File {}, deleted! ", filePath);

        return isDeleted;
    }

    /**
     * @param file to delete
     * @return true if file is deleted, false otherwise
     * @throws IOException
     */
    public static boolean deleteFile(File file) throws IOException {
        boolean isDeleted = false;

        if (file.exists()) {
            isDeleted = file.delete();
        }

        if (isDeleted) {
            log.debug("File {}, deleted! ", file.getPath());
        }
        return isDeleted;
    }

    public static ByteArrayOutputStream zipFiles(File[] filesOutputResults) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(bos);

        for (File file : filesOutputResults) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipEntry.setSize(file.length());
            zipOutputStream.putNextEntry(zipEntry);
            StreamUtils.copy(new FileInputStream(file), zipOutputStream);
            zipOutputStream.closeEntry();
        }
        zipOutputStream.finish();
        zipOutputStream.close();

        return bos;
    }

    /**
     *
     * @param outputFiles
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream zipOutputFiles(List<OutputFile> outputFiles) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(bos);
        for (OutputFile outputFile : outputFiles) {
            byte[] fileData = outputFile.getData();
            String fileName = outputFile.getFileName().trim();
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(fileData);
            zipOutputStream.closeEntry();
        }
        zipOutputStream.finish();
        zipOutputStream.close();
        return bos;
    }

    /**
     * Generate an archive (.zip) containing files and sub-directories
     * @param dirToZip
     * @return ByteArrayOutputStream
     * @throws IOException
     */
    public static ByteArrayOutputStream zipDir(File dirToZip) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        File[] filesOutputResults = dirToZip.listFiles();
        ZipOutputStream zipOutputStream = new ZipOutputStream(bos);
        for (File file : filesOutputResults) {
            if (file.isDirectory()) {
                addDir(zipOutputStream, file);
                continue;
            }
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipEntry.setSize(file.length());
            zipOutputStream.putNextEntry(zipEntry);
            StreamUtils.copy(new FileInputStream(file), zipOutputStream);
            zipOutputStream.closeEntry();
        }
        zipOutputStream.finish();
        zipOutputStream.close();

        return bos;
    }

    private static void addDir(ZipOutputStream zipOutputStream, File dir) throws IOException {
        File[] filesOutputResults = dir.listFiles();

        for (File file : filesOutputResults) {
            if (file.isDirectory()) {
                addDir(zipOutputStream, file);
                continue;
            }
            ZipEntry zipEntry = new ZipEntry(dir.getName().concat(File.separator).concat(file.getName()));
            zipEntry.setSize(file.length());
            zipOutputStream.putNextEntry(zipEntry);
            StreamUtils.copy(new FileInputStream(file), zipOutputStream);
            zipOutputStream.closeEntry();
        }
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
            log.error("Exception: ", e);
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
                log.info(line);
                writer.write(line);
                writer.newLine();
            }
            writer.close();
            return new File(filePath);
        } catch (IOException e) {
            log.error("Exception: ", e);
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
                    log.info(line);
                    writer.write(line);
                    writer.newLine();
                }
            }
            writer.close();
            return new File(filePath);
        } catch (IOException e) {
            log.error("Exception: ", e);
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
                    log.info(line);
                    writer.write(line);
                    writer.newLine();
                }
            }
            writer.close();
            return new File(filePath);
        } catch (IOException e) {
            log.error("Exception writing file: " + filePath, e);
            return null;
        }
    }

    public static void createDir(String directory) {
        try {
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
                log.info(" Directory {} created ", directory);
            }
        } catch (Exception ex) {
            log.error("Exception: ", ex);
        }
    }

    public static void saveFileOnFs(String inputFileName, byte[] data) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(inputFileName)) {
            fos.write(data);
            log.debug("Input File: {}, Saved on fileSystem!", inputFileName);
        } catch (Exception e) {
            String errMsg = "Error saving file: " + inputFileName;
            log.error(errMsg);
            throw new Exception(errMsg);
        }
    }

    /**
     * @param rootDir root directory to start the search
     * @return list the AbsolutePath of all the directories present under the directory given as input parameter
     */
    public static List<File> listDir(String rootDir) {
        File directory = new File(rootDir);
        File[] fList = directory.listFiles(
            new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            }
        );

        List<File> resultList = new ArrayList<File>(Arrays.asList(fList));
        for (File file : fList) {
            resultList.addAll(listDir(file.getAbsolutePath()));
        }

        return resultList;
    }

    /**
     * @param rootDir root directory to start the search
     * @param dirNameToSearch name of the directory to search
     * @return null if no directory was found with specified by the param dirNameToSearch or the absolutePath of the directory found.
     */
    public static File searchDir(String rootDir, String dirNameToSearch) {
        List<File> resultList = listDir(rootDir);
        List<File> fileList = resultList
            .stream()
            .filter(f -> {
                // log.debug("File Path: {} , File Name : {}" , f.getPath(), f.getName());
                return f.getName().equals(dirNameToSearch);
            })
            .collect(Collectors.toList());

        if (fileList.isEmpty()) {
            return null;
        } else {
            // Exist only one dir with name dirNameToSearch
            return fileList.get(0);
        }
    }

    /**
     * @param rootDir represents the directory to be traversed.
     * @param level 0 identifies the level of directory grafting: 0 for the parent directory, 1 the child directory, and so on
     * @param mapNameFile map containing the filename as the key. The key is valued by concatenating the subdirectory name with the file name
     */
    public static void readContentDir(File rootDir, int level, Map<String, File> mapNameFile) {
        log.info("START read all file in: {} ", rootDir);
        File[] allFiles = rootDir.listFiles();
        for (File file : allFiles) {
            if (file.isDirectory()) {
                log.debug(" --- Is Directory with Name: " + file.getName());
                FileUtils.readContentDir(file, level + 1, mapNameFile);
            } else {
                log.debug(" --- Is File with Name: " + file.getName());
                String name = (level > 0) ? rootDir.getName().concat("_").concat(file.getName()) : file.getName();
                mapNameFile.put(name, file);
            }
        }
        log.info("END read content in Dir: {}", rootDir);
    }

    public static boolean filterFileByNameExtension(File file, List<String> extensions) {
        for (String ext : extensions) {
            if (file.getName().endsWith(ext)) {
                log.debug(" --- File Name: {}, extensions: {} , return true", file.getName(), extensions.toString());
                return true;
            }
        }
        log.debug(" --- File Name: {}, extensions: {} , return false", file.getName(), extensions.toString());
        return false;
    }

    public static void main(String args[]) {
        int test = 0;
        if (test == 0) {
            //File file = new File("C:\\temp\\2_hr_dx_01_2020_red_output.xlsx");
            //File file = new File ("C:\\temp\\branches_location1.csv");
            //File file = new File ("C:\\temp\\branchExtension.json");
            //File file = new File ("C:\\temp\\response.ods");
            //File file = new File ("C:\\temp\\Per Usare Zoom.pdf");
            //File file = new File ("C:\\temp\\Installazione_DOC.docx");
            //File file = new File ("C:\\temp\\modelTR1.h5");
            File file = new File("C:\\temp\\CompareNetwork_T23_Tools\\HR_TX\\T23\\Location1_Koprivnica.m");

            Path path = file.toPath();
            String ext = FileUtils.getFileExtension(file);
            try {
                String mimeType = FileUtils.probeContentType(path);
                String fromProbe = Files.probeContentType(path);
                System.out.println("ext: " + ext + ", contentType: " + mimeType);
                System.out.println(" fromProbe " + fromProbe);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (test == 1) {
            String zipArchiveName = "C:\\temp\\Test1.zip";
            File dirToZip = new File("C:\\temp\\PRAPARAZIONE_TEST");
            try {
                System.out.println(" Create archive: " + zipArchiveName);
                ByteArrayOutputStream baos = FileUtils.zipDir(dirToZip);
                System.out.println("  Archive size:  " + baos.size());

                try {
                    FileOutputStream fos = new FileOutputStream(new File(zipArchiveName));
                    // Put data in your baos
                    baos.writeTo(fos);
                    System.out.println("  Archive created succesfully:  ");
                } catch (IOException ioe) {
                    // Handle exception here
                    ioe.printStackTrace();
                } finally {
                    baos.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (test == 2) {
            String rootDir = "C:\\ATSIM\\WP3\\T33\\005dae36-2948-4c3e-8eb3-33e8978db21e";
            File found = FileUtils.searchDir(rootDir, "Results");
            System.out.println("Found File: {}" + found.toString());
        }

        if (test == 3) {
            String rootDir = "C:\\ATSIM\\WP4\\T44\\655d675b-df87-4305-ab71-301868308446\\output_data";
            Map<String, File> mapAllFile = new HashMap<>();
            FileUtils.readContentDir(new File(rootDir), 0, mapAllFile);
            System.out.println(" Directory contains before: ");
            System.out.println("  " + Arrays.deepToString(mapAllFile.entrySet().stream().toArray()));
            System.out.println("=========================== ");

            List<String> extensions = Stream.of(".csv", ".xlsx").collect(Collectors.toList());

            /*  mapAllFile.entrySet().stream().filter( f -> FileUtils.filterFileByNameExtension(f.getValue(), extensions) )
                .map( Map.Entry::getKey )
                .collect( Collectors.toList() );
                */

            Map<String, File> mapFilteredFileList = mapAllFile
                .entrySet()
                .stream()
                .filter(f -> FileUtils.filterFileByNameExtension(f.getValue(), extensions))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

            System.out.println(" Directory contains after: ");
            System.out.println("  " + Arrays.deepToString(mapFilteredFileList.entrySet().stream().toArray()));
            System.out.println("=========================== ");
        }
        if (test == 4) {
            Map<String, Object> mapObj = new HashMap<String, Object>();
            mapObj.put("season", "SU");
            mapObj.put("year", 2030);
            mapObj.put("flex", 1);

            String descr = String.join(
                ",",
                (String) mapObj.get("season"),
                String.valueOf(mapObj.get("year")),
                (String) mapObj.get("case_name")
            );
            System.out.println("=========================== " + descr);
        }

        if (test == 5) {
            String filePath = "C:\\temp\\UK_Dx_01_2040__S_WithFlex_T41 (1).m";

            String ext = FileUtils.getFileExtension(filePath);

            System.out.println(" File extension: " + ext);
        }
    }
}
