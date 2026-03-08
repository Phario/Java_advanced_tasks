package pl.pwr.ite.dynak.zipper;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
    public static String generateHash(String filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        }

        return bytesToHex(digest.digest());
    }


    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static boolean checkHash(String filePath, String hashFilePath) {
        try {
            String expectedHash = new String(Files.readAllBytes(Path.of(hashFilePath))).trim();

            String actualHash = generateHash(filePath);

            return expectedHash.equalsIgnoreCase(actualHash);

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }
}
