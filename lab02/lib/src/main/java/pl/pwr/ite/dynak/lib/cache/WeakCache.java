package pl.pwr.ite.dynak.lib.cache;

import pl.pwr.ite.dynak.lib.parser.IFileParser;

import java.io.File;
import java.io.IOException;
import java.util.WeakHashMap;

// T - cache result
public class WeakCache<T> {
    private final WeakHashMap<File, T> cache = new WeakHashMap<>();
    private final IFileParser<T> parser;

    public WeakCache(IFileParser<T> parser) {
        this.parser = parser;
    }

    public WeakCacheResult<T> get(File file) throws IOException {
        T data = cache.get(file);

        if (data != null) {
            return new WeakCacheResult<>(true, data);
        }

        data = parser.parse(file);
        cache.put(file, data);
        return new WeakCacheResult<>(false, data);
    }

    public void put(File file, T data) {
        cache.put(file, data);
    }
}
