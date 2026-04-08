package pl.pwr.ite.dynak.loader;

import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomClassLoader extends ClassLoader {
    private final Path basePath;

    public CustomClassLoader(Path basePath) {
        if (!Files.isDirectory(basePath)) {
            throw new IllegalArgumentException("Path is not a directory");
        }
        this.basePath = basePath;
    }

    @Override
    public Class<?> findClass(String basePath) throws ClassNotFoundException {
        Path classFile = Paths.get(
                basePath + FileSystems.getDefault().getSeparator()
                + basePath.replace(".", FileSystems.getDefault().getSeparator())
                + ".class"
        );
        byte[] buffer;
        try {
            buffer = Files.readAllBytes(classFile);
            return defineClass(basePath, buffer, 0, buffer.length);
        }
        catch (Exception e) {
            throw new ClassNotFoundException(basePath);
        }

    }
}
