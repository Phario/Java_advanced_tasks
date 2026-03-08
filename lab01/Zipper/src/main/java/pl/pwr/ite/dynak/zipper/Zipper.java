package pl.pwr.ite.dynak.zipper;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper {
    public static void zipFiles(List<String> files, String outputLocation) throws IOException {
        final FileOutputStream outputStream = new FileOutputStream(Paths.get(outputLocation).toFile());
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

        for (String file : files) {
            File fileToZip = new File(file);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOutputStream.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, length);
            }
            fis.close();
        }

        zipOutputStream.close();
        outputStream.close();
    }

    public static void zipDirectory(String directory, String outputLocation) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(Paths.get(outputLocation).toFile());
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        zipFile(new File(directory), directory, zipOutputStream);
        zipOutputStream.close();
        outputStream.close();
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream outputLocation) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                outputLocation.putNextEntry(new ZipEntry(fileName));
                outputLocation.closeEntry();
            } else {
                outputLocation.putNextEntry(new ZipEntry(fileName + "/"));
                outputLocation.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), outputLocation);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        outputLocation.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            outputLocation.write(bytes, 0, length);
        }
        fis.close();
    }

    public static void zip(List<String> sources, String outputLocation) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(Paths.get(outputLocation).toFile());
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            for (String sourcePath : sources) {
                File fileToZip = new File(sourcePath);
                zipFile(fileToZip, fileToZip.getName(), zipOut);
            }
        }
    }

}
