package pl.pwr.ite.dynak.loader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class CustomClassLoader extends ClassLoader {
    private final Path basePath;

    public CustomClassLoader(Path basePath) {
        if (!Files.isDirectory(basePath)) {
            throw new IllegalArgumentException("Path is not a directory");
        }
        this.basePath = basePath;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        String relativePath = name.replace('.', File.separatorChar) + ".class";
        Path classFile = basePath.resolve(relativePath);

        try {
            byte[] bytes = Files.readAllBytes(classFile);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            throw new ClassNotFoundException(name, e);
        }
    }
}
