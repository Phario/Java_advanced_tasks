package pl.pwr.ite.dynak.zipper;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper {

    public static void zip(List<String> sources, String outputLocation) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(Paths.get(outputLocation).toFile());
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            for (String sourcePath : sources) {
                File fileToZip = new File(sourcePath);
                zipFile(fileToZip, fileToZip.getName(), zipOut);
            }
        }
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }

        if (fileToZip.isDirectory()) {
            if (!fileName.endsWith("/")) {
                fileName += "/";
            }
            zipOut.putNextEntry(new ZipEntry(fileName));
            zipOut.closeEntry();

            File[] children = fileToZip.listFiles();
            if (children != null) {
                for (File child : children) {
                    zipFile(child, fileName + child.getName(), zipOut);
                }
            }
            return;
        }

        try (FileInputStream fis = new FileInputStream(fileToZip)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        }
    }
}
